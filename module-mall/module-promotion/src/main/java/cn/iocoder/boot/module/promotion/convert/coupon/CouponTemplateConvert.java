package cn.iocoder.boot.module.promotion.convert.coupon;

import cn.hutool.core.map.MapUtil;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponTemplatePageReqVO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponTemplateRespVO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponTemplatePageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * @author xiaosheng
 */
@Mapper
public interface CouponTemplateConvert {
    CouponTemplateConvert INSTANCE = Mappers.getMapper(CouponTemplateConvert.class);

    CouponTemplatePageReqVO convert(AppCouponTemplatePageReqVO pageReqVO, List<Integer> canTakeTypes, Integer productScope, Long productScopeValue);

    PageResult<AppCouponTemplateRespVO> convertAppPage(PageResult<CouponTemplateDO> pageResult);

    default PageResult<AppCouponTemplateRespVO> convertAppPage(PageResult<CouponTemplateDO> pageResult, Map<Long, Boolean> canCanTakeMap) {
        PageResult<AppCouponTemplateRespVO> result = convertAppPage(pageResult);
        copyTo(result.getList(), canCanTakeMap);
        return result;
    }

    default void copyTo(List<AppCouponTemplateRespVO> list, Map<Long, Boolean> userCanTakeMap) {
        for (AppCouponTemplateRespVO template : list) {
            // 检查已领取数量是否超过限领数量
            template.setCanTake(MapUtil.getBool(userCanTakeMap, template.getId(), false));
        }
    }
}
