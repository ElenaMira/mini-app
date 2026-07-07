package cn.iocoder.boot.module.promotion.service.coupon;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponTemplatePageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;

/**
 * @author xiaosheng
 */
public interface CouponTemplateService {
    /**
     * 基于vo获取分页CouponTemplateDO
     * @param pageReqVO
     * @return
     */
    PageResult<CouponTemplateDO> getCouponTemplatePage(CouponTemplatePageReqVO pageReqVO);

    /**
     * 基于限领个数字段判断是否不限制每人领取数量
     * @param takeLimitCount
     * @return
     */
    Boolean isTakeLimitCountUnlimited(Integer takeLimitCount);
}
