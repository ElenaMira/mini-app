package cn.iocoder.boot.module.promotion.service.coupon;

import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Override
    public Long getUnusedCouponCount(Long loginUserId) {
        return 0L;
    }
}
