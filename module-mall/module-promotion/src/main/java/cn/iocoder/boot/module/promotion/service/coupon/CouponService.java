package cn.iocoder.boot.module.promotion.service.coupon;

import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface CouponService {
    /**
     * 获取未使用的优惠卷
     * @param loginUserId   用户ID
     * @return
     */
    Long getUnusedCouponCount(@NotNull Long loginUserId);
}
