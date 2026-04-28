package cn.iocoder.boot.springsecurity.system.framework.sms.core.client;

import cn.iocoder.boot.springsecurity.system.framework.sms.core.property.SmsChannelProperties;

/**
 * @author xiaosheng
 */
public interface SmsClientFactory {
    /**
     *
     * @param smsChannelProperties 创建客户端必要的字段
     * @return
     */
    SmsClient createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties);
}
