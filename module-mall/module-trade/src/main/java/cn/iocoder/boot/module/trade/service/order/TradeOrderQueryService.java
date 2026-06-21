package cn.iocoder.boot.module.trade.service.order;

import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface TradeOrderQueryService {
    /**
     * 【会员】获得交易订单数量
     *
     * @param loginUserId   用户ID
     * @param status 订单状态。如果为空，则不进行筛选
     * @param commonStatus  评价状态。如果为空，则不进行筛选
     * @return  订单数
     */
    Long getOrderCount(@NotNull Long loginUserId, Integer status, Boolean commonStatus);
}
