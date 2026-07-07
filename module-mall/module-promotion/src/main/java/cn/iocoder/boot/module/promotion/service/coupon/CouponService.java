package cn.iocoder.boot.module.promotion.service.coupon;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取用户是否可以领取优惠券
     *
     * @param loginUserId    用户编号
     * @param templates 优惠券列表
     * @return 是否可以领取
     */
    Map<Long, Boolean> getUserCanCanTakeMap(@NotNull Long loginUserId, List<CouponTemplateDO> templates);

    /**
     * 统计会员领取优惠券的数量
     *
     * @param templateIds 优惠券模板编号列表
     * @param loginUserId      用户编号
     * @return 领取优惠券的数量
     */
    Map<Long, Integer> getTakeCountMapByTemplateIds(Collection<Long> templateIds, Long loginUserId);
}
