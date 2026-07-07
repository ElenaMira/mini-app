package cn.iocoder.boot.module.promotion.service.coupon;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponTemplatePageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;
import cn.iocoder.boot.module.promotion.dal.mysql.coupon.CouponTemplateMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {
    @Resource
    private CouponTemplateMapper couponTemplateMapper;

    @Override
    public PageResult<CouponTemplateDO> getCouponTemplatePage(CouponTemplatePageReqVO pageReqVO) {
        return couponTemplateMapper.selectPage(pageReqVO);
    }

    @Override
    public Boolean isTakeLimitCountUnlimited(Integer takeLimitCount) {
        return CouponTemplateDO.TAKE_LIMIT_COUNT_MAX.equals(takeLimitCount);
    }
}
