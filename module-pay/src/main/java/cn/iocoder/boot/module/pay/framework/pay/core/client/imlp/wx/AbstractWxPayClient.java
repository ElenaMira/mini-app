package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx;

import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.AbstractPayClient;

/**
 * 功能描述:
 *
 * @author xiaosinian
 */
public abstract class AbstractWxPayClient extends AbstractPayClient<WxPayClientConfig> {

    public AbstractWxPayClient(Long channelId, String channelCode, WxPayClientConfig config) {
        super(channelId, channelCode, config);
    }
}
