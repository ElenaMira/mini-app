package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx;

import cn.iocoder.boot.module.pay.enums.pay.PayChannelEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaosheng
 */
@Slf4j
public class WxLitePayClient extends WxPubPayClient{

    public WxLitePayClient(Long channelId, WxPayClientConfig config) {
        super(channelId, PayChannelEnum.WX_LITE.getCode(), config);
    }
}
