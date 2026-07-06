package cn.iocoder.boot.module.promotion.service.coupon;

import cn.iocoder.boot.module.promotion.dal.mysql.coupon.CouponMapper;
import cn.iocoder.boot.module.promotion.enums.coupon.CouponStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Resource
    private CouponMapper couponMapper;

    @Override
    public Long getUnusedCouponCount(Long loginUserId) {
        return couponMapper.selectCountByUserIdAndStatus(loginUserId, CouponStatusEnum.UNUSED.getStatus());
    }
}
