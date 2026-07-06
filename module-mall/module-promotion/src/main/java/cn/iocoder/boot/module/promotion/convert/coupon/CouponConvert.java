package cn.iocoder.boot.module.promotion.convert.coupon;

import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponPageReqVO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponPageReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * @author xiaosheng
 */
@Mapper
public interface CouponConvert {
    CouponConvert INSTANCE = Mappers.getMapper(CouponConvert.class);

    CouponPageReqVO convert(AppCouponPageReqVO pageReqVO, Set<Long> singleton);
}
