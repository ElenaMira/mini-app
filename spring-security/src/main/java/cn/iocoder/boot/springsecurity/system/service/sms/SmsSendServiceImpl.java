package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.springsecurity.common.core.KeyValue;
import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsChannelDO;
import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsTemplateDO;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClient;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.boot.springsecurity.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.boot.springsecurity.system.mq.producer.sms.SmsProducer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.system.enums.ErrorCodeConstant.*;

/**
 * @author xiaosheng
 */
@Slf4j
@Service
public class SmsSendServiceImpl implements SmsSendService{
    @Resource
    private SmsTemplateService smsTemplateService;
    @Resource
    private SmsChannelService smsChannelService;
    @Resource
    private SmsProducer smsProducer;
    @Override
    public Long sendSingleSms(String mobile, Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        //校验短信模板是否合法
        SmsTemplateDO smsTemplateDO = validateSmsTemplate(templateCode);
        // 校验短信渠道是否合法
        SmsChannelDO channelDO = validateSmsChannel(smsTemplateDO.getChannelId());

        //校验手机号
        mobile = validateMobile(mobile);
        //将模板参数转化为keyValue格式存储(兼容如腾讯云这种按顺序的)
        List<KeyValue<String, Object>> newTemplateParams = buildTemplateParams(smsTemplateDO,templateParams);

        //创建发送日志。如果模板被禁用，则不发送短信，todo只记录日志,暂时用固定值作为日志编号
        boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(smsTemplateDO.getStatus())
                && CommonStatusEnum.ENABLE.getStatus().equals(channelDO.getStatus());
//        String content = smsTemplateService.formatSmsTemplateContent(template.getContent(), templateParams);
//        Long sendLogId = smsLogService.createSmsLog(mobile, userId, userType, isSend, template, content, templateParams);

        //发送MQ消息,异步执行发送短信
        if (isSend) {
            smsProducer.sendSmsSendMessage(666L, mobile, smsTemplateDO.getChannelId(),
                    smsTemplateDO.getApiTemplateId(), newTemplateParams);
        }
        // 返回日志ID
        return 0L;
    }

    @Override
    public void doSendSms(SmsSendMessage message) {
        //获取渠道对应的SmsClient客户端
        SmsClient smsClient = smsChannelService.getSmsClient(message.getChannelId());
        Assert.notNull(smsClient, "短信客户端({}) 不存在", message.getChannelId());

        try{
            SmsSendRespDTO smsSendRespDTO = smsClient.sendSms(message.getLogId(), message.getMobile(), message.getApiTemplateId(),
                    message.getTemplateParams());
        } catch (Throwable e) {
            log.error("[doSendSms][发送短信异常，日志编号({})]", message.getLogId(), e);
        }
    }

    /**
     *  将Map类型 -> List<KeyValue>类型 比如: [KeyValue(key="code", value="1234")]
     * @param smsTemplateDO
     * @param templateParams
     * @return
     */
    @VisibleForTesting
    List<KeyValue<String, Object>> buildTemplateParams(SmsTemplateDO smsTemplateDO, Map<String,Object> templateParams){
        return smsTemplateDO.getParams().stream().map(key->{
            Object value = templateParams.get(key);
            if(value==null){
                throw exception(SMS_SEND_MOBILE_TEMPLATE_PARAM_MISS,key);
            }
            return new KeyValue<>(key,value);
        }).collect(Collectors.toList());
    }
    @VisibleForTesting
    SmsTemplateDO validateSmsTemplate(String templateCode){
        SmsTemplateDO smsTemplateDO = smsTemplateService.getSmsTemplateByCodeFromCache(templateCode);
        if(smsTemplateDO==null){
            throw exception(SMS_SEND_TEMPLATE_NOT_EXISTS);
        }
        return smsTemplateDO;
    }
    @VisibleForTesting
    SmsChannelDO validateSmsChannel(Long channelId){
        //底层使用channel的主键查询
        SmsChannelDO channelDO = smsChannelService.getChannel(channelId);
        if(channelDO==null){
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
        return channelDO;
    }
    @VisibleForTesting
    public String validateMobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            throw exception(SMS_SEND_MOBILE_NOT_EXISTS);
        }
        return mobile;
    }
}
