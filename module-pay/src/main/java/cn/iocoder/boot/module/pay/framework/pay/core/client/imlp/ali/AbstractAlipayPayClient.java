package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.ali;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderUnifiedReqDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.AbstractPayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.Getter;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.Objects;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;
import static cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.ali.AlipayPayClientConfig.MODE_CERTIFICATE;

/**
 * @author xiaosheng
 */
public abstract class AbstractAlipayPayClient extends AbstractPayClient<AlipayPayClientConfig> {
    @Getter
    protected DefaultAlipayClient client;

    public AbstractAlipayPayClient(Long channelId, String channelCode, AlipayPayClientConfig config) {
        super(channelId, channelCode, config);
    }

    @Override
    @SneakyThrows
    protected void doInit() {
        AlipayConfig alipayConfig = new AlipayConfig();
        BeanUtil.copyProperties(config, alipayConfig, false);
        this.client = new DefaultAlipayClient(alipayConfig);
    }
    // ============ 支付相关 ==========

    /**
     * 构造支付关闭的 {@link PayOrderRespDTO} 对象
     *
     * @return 支付关闭的 {@link PayOrderRespDTO} 对象
     */
    protected PayOrderRespDTO buildClosedPayOrderRespDTO(PayOrderUnifiedReqDTO reqDTO, AlipayResponse response) {
        Assert.isFalse(response.isSuccess());
        return PayOrderRespDTO.closedOf(response.getSubCode(), response.getSubMsg(),
                reqDTO.getOutTradeNo(), response);
    }
    
    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) throws Throwable {
        // 1.1 构建 AlipayTradeRefundModel 请求
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        // 1.2 构建 AlipayTradeQueryRequest 请求
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        AlipayTradeQueryResponse response;
        if (Objects.equals(config.getMode(), MODE_CERTIFICATE)) {
            // 证书模式
            response = client.certificateExecute(request);
        } else {
            response = client.execute(request);
        }
        if (!response.isSuccess()) { // 不成功，例如说订单不存在
            return PayOrderRespDTO.closedOf(response.getSubCode(), response.getSubMsg(),
                    outTradeNo, response);
        }
        // 2.2 解析订单的状态
        Integer status = parseStatus(response.getTradeStatus());
        Assert.notNull(status, () -> {
            throw new IllegalArgumentException(StrUtil.format("body({}) 的 trade_status 不正确", response.getBody()));
        });
        return PayOrderRespDTO.of(status, response.getTradeNo(), response.getBuyerUserId(), LocalDateTimeUtil.of(response.getSendPayDate()),
                outTradeNo, response);
    }
    private static Integer parseStatus(String tradeStatus) {
        return Objects.equals("WAIT_BUYER_PAY", tradeStatus) ? PayOrderStatusEnum.WAITING.getStatus()
                : ObjectUtils.equalsAny(tradeStatus, "TRADE_FINISHED", "TRADE_SUCCESS") ? PayOrderStatusEnum.SUCCESS.getStatus()
                : Objects.equals("TRADE_CLOSED", tradeStatus) ? PayOrderStatusEnum.CLOSED.getStatus() : null;
    }


    //  ========== 各种工具方法 ==========
    protected String formatAmount(Integer amount) {
        return String.valueOf(amount / 100.0);
    }
    protected String formatTime(LocalDateTime time) {
        return LocalDateTimeUtil.format(time, NORM_DATETIME_FORMATTER);
    }
}
