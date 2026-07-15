package cn.iocoder.boot.module.trade.service.order;

import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface TradeOrderUpdateService {
    /**
     * 更新交易订单已支付
     *
     * @param id         交易订单编号
     * @param payOrderId 支付订单编号
     */
    void updateOrderPaid(Long id, @NotNull Long payOrderId);
}
