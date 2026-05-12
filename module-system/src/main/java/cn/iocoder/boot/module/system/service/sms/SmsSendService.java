package cn.iocoder.boot.module.system.service.sms;

import cn.iocoder.boot.module.system.mq.message.sms.SmsSendMessage;

import java.util.Map;

/**
 * @author xiaosheng
 */
public interface SmsSendService {
    Long sendSingleSms(String mobile, Long userId, Integer userType,
                       String templateCode, Map<String, Object> templateParams);

    void doSendSms(SmsSendMessage message);
}
