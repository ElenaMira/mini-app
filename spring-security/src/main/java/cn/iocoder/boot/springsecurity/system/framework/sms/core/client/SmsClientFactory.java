package cn.iocoder.boot.springsecurity.system.framework.sms.core.client;

import cn.iocoder.boot.springsecurity.system.framework.sms.core.property.SmsChannelProperties;

/**
 * @author xiaosheng
 */
public interface SmsClientFactory {
    SmsClient createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties);
}
