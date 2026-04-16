package cn.iocoder.boot.springsecurity.system.framework.sms.core.client;

import cn.iocoder.boot.springsecurity.common.core.KeyValue;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.dto.SmsSendRespDTO;

import java.util.List;

/**
 * 短信客户端，用于对接各短信平台的 SDK，实现短信发送等功能
 * @author xiaosheng
 */
public interface SmsClient {
    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();
    /**
     * 发送消息
     *
     * @param logId 日志编号
     * @param mobile 手机号
     * @param apiTemplateId 短信 API 的模板编号
     * @param templateParams 短信模板参数。通过 List 数组，保证参数的顺序
     * @return 短信发送结果
     */
    SmsSendRespDTO sendSms(Long logId, String mobile, String apiTemplateId,
                           List<KeyValue<String, Object>> templateParams) throws Throwable;

    
}
