package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.boot.springsecurity.common.validation.InEnum;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;
import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeSendReqDTO;
import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsCodeDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.sms.SmsCodeMapper;
import cn.iocoder.boot.springsecurity.system.enums.sms.SmsSceneEnum;
import cn.iocoder.boot.springsecurity.system.framework.sms.config.SmsCodeProperties;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.common.uitl.date.DateUtils.isToday;
import static cn.iocoder.boot.springsecurity.system.enums.ErrorCodeConstant.SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY;
import static cn.iocoder.boot.springsecurity.system.enums.ErrorCodeConstant.SMS_CODE_SEND_TOO_FAST;

/**
 * @author xiaosheng
 */
@Service
public class SmsCodeServiceImpl implements SmsCodeService{
    @Resource
    private SmsCodeMapper smsCodeMapper;
    @Resource
    private SmsCodeProperties smsCodeProperties;
    @Resource
    private SmsSendService smsSendService;

    @Override
    public void sendSmsCode(SmsCodeSendReqDTO reqDTO) {
        SmsSceneEnum sceneEnum = SmsSceneEnum.getCodeByScene(reqDTO.getScene());
        Assert.notNull(sceneEnum, "验证码场景({}) 查找不到配置", reqDTO.getScene());

        //创建验证码模板
        String code = createSmsCode(reqDTO.getMobile(), reqDTO.getScene(), reqDTO.getCreateIp());

        //发送验证码
        smsSendService.sendSingleSms(reqDTO.getMobile(), null, null,
                sceneEnum.getTemplateCode(), MapUtil.of("code", code));
    }

    private String createSmsCode(String mobile,  Integer scene, String createIp) {
        //校验当前手机号是否可以发送验证码，不用筛选场景
        SmsCodeDO lastSmsCodeDO = smsCodeMapper.selectByLastMobile(mobile, null, null);
        if (lastSmsCodeDO!=null){
            if(LocalDateTimeUtil.between(lastSmsCodeDO.getCreateTime(), LocalDateTime.now()).toMillis()
                <smsCodeProperties.getSendFrequency().toMillis()){
                throw exception(SMS_CODE_SEND_TOO_FAST);
            }
            if (isToday(lastSmsCodeDO.getUpdateTime())&&
                lastSmsCodeDO.getTodayIndex()>= smsCodeProperties.getSendMaximumQuantityPerDay()){
                throw exception(SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY);
            }
            // TODO 提升，每个 IP 每天可发送数量
            // TODO 提升，每个 IP 每小时可发送数量
        }

        //创建验证码
        // "%04d"表示生成的字符串最小4位数不够前面补0
        String code = String.format("%0"+smsCodeProperties.getBeginCode().toString().length()+"d",
                randomInt(smsCodeProperties.getBeginCode(),smsCodeProperties.getEndCode()));
        SmsCodeDO smsCodeDO = SmsCodeDO.builder().mobile(mobile).code(code).createIp(createIp).scene(scene)
                .todayIndex(lastSmsCodeDO != null && isToday(lastSmsCodeDO.getUpdateTime()) ? lastSmsCodeDO.getTodayIndex() + 1 : 1)
                .used(false).build();
        smsCodeMapper.insert(smsCodeDO);
        return code;
    }
}
