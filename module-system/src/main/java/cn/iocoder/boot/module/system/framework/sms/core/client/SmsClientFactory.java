package cn.iocoder.boot.module.system.framework.sms.core.client;

import cn.iocoder.boot.module.system.framework.sms.core.property.SmsChannelProperties;

/**
 * @author xiaosheng
 */
public interface SmsClientFactory {
    /**
     *  创建或更新短信 Client
     * @param smsChannelProperties 配置对象
     * @return 短信客户端
     */
    SmsClient createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties);
}
