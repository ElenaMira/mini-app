package org.example.module.pay.framework.pay.core.client;

public interface PayClientFactory {
    /**
     * 创建支付客户端
     *
     * @param channelId 渠道编号
     * @param channelCode 渠道编码
     * @param config 支付配置
     * @return 支付客户端
     */
    <Config extends PayClientConfig> PayClient createOrUpdatePayClient(Long channelId, String channelCode,
                                                                       Config config);
}
