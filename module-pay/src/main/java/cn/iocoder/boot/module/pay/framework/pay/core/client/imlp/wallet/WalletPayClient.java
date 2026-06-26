package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wallet;

import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.AbstractPayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.NonePayClientConfig;

/**
 * @author xiaosheng
 */
public class WalletPayClient extends AbstractPayClient<NonePayClientConfig> {

    public static final String WALLET_ID_KEY = "walletId";

    public WalletPayClient(Long channelId, String channelCode, NonePayClientConfig config) {
        super(channelId, channelCode, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) throws Throwable {
        return null;
    }
}
