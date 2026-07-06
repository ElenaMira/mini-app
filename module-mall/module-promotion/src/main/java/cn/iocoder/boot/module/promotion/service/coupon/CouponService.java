package cn.iocoder.boot.module.promotion.service.coupon;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
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

    /**
     * 获得优惠劵分页
     *
     * @param pageReqVO 分页查询
     * @return 优惠劵分页
     */
    PageResult<CouponDO> getCouponPage(CouponPageReqVO pageReqVO);
}
