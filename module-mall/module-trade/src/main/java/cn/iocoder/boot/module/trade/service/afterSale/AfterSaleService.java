package cn.iocoder.boot.module.trade.service.afterSale;

import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface AfterSaleService {
    /**
     * 【会员】获得正在进行中的售后订单数量
     *
     * @param loginUserId 用户编号
     * @return 数量
     */
    Long getApplyingAfterSaleCount(@NotNull Long loginUserId);
}
