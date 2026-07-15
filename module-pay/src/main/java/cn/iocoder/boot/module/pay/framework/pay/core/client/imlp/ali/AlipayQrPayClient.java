package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.ali;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.enums.pay.PayChannelEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderUnifiedReqDTO;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.ali.AlipayPayClientConfig.MODE_CERTIFICATE;

/**
 * @author xiaosheng
 */
@Slf4j
public class AlipayQrPayClient extends AbstractAlipayPayClient{
    public AlipayQrPayClient(Long channelId, AlipayPayClientConfig config) {
        super(channelId, PayChannelEnum.ALIPAY_QR.getCode(), config);
    }

    // todo完善二维码付款
    @Override
    protected PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO) throws Throwable {
        return null;
    }
}
