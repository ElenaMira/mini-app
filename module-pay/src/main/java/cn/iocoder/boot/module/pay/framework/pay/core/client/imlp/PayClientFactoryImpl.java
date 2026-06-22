package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.boot.module.pay.enums.pay.PayChannelEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientConfig;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientFactory;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx.WxLitePayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx.WxPubPayClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static cn.iocoder.boot.module.pay.enums.pay.PayChannelEnum.*;

public class PayClientFactoryImpl implements PayClientFactory {
    /**
     * 支付客户端 Map
     *
     * key：渠道编号
     */
    private final ConcurrentMap<Long,AbstractPayClient<?>> clients = new ConcurrentHashMap<>();
    /**
     * 支付客户端 Class Map
     */
    private final Map<PayChannelEnum, Class<? extends PayClient<?>>> clientClass = new ConcurrentHashMap<>();


    public PayClientFactoryImpl() {
        // 微信支付客户端
        clientClass.put(WX_PUB, WxPubPayClient.class);
        clientClass.put(WX_LITE, WxLitePayClient.class);
//        clientClass.put(WX_APP, WxAppPayClient.class);
//        clientClass.put(WX_BAR, WxBarPayClient.class);
//        clientClass.put(WX_NATIVE, WxNativePayClient.class);
//        clientClass.put(WX_WAP, WxWapPayClient.class);
//        // 支付包支付客户端
//        clientClass.put(ALIPAY_WAP, AlipayWapPayClient.class);
//        clientClass.put(ALIPAY_QR, AlipayQrPayClient.class);
//        clientClass.put(ALIPAY_APP, AlipayAppPayClient.class);
//        clientClass.put(ALIPAY_PC, AlipayPcPayClient.class);
//        clientClass.put(ALIPAY_BAR, AlipayBarPayClient.class);
//        // 钱包支付客户端
//        clientClass.put(WALLET, WalletPayClient.class);
//        // Mock 支付客户端
//        clientClass.put(MOCK, MockPayClient.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Config extends PayClientConfig> PayClient createOrUpdatePayClient(Long channelId, String channelCode, Config config) {
        AbstractPayClient<Config> client = (AbstractPayClient<Config>) clients.get(channelId);
        if (client == null) {
            client = this.createPayClient(channelId, channelCode, config);
            client.init();
            clients.put(client.getId(), client);
        } else {
            client.refresh(config);
        }
        return client;
    }

    @SuppressWarnings("unchecked")
    private <Config extends PayClientConfig> AbstractPayClient<Config> createPayClient(Long channelId, String channelCode, Config config) {
        PayChannelEnum channelEnum = PayChannelEnum.getByCode(channelCode);
        Assert.notNull(channelEnum, String.format("支付渠道(%s) 为空", channelCode));
        Class<?> payClientClass = clientClass.get(channelEnum);
        Assert.notNull(payClientClass,String.format("支付渠道(%s) Class 为空", channelCode));
        //动态创建对应channelEnum的Client实例
        return (AbstractPayClient<Config>) ReflectUtil.newInstance(payClientClass, channelId, config);
    }
}
