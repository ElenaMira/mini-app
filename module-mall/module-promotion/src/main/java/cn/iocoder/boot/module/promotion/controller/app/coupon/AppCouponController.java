package cn.iocoder.boot.module.promotion.controller.app.coupon;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponPageReqVO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponRespVO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponTakeByInviteReqVO;
import cn.iocoder.boot.module.promotion.convert.coupon.CouponConvert;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
import cn.iocoder.boot.module.promotion.service.coupon.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 优惠劵")
@RestController
@RequestMapping("/promotion/coupon")
@Validated
public class AppCouponController {
    @Resource
    private CouponService couponService;

//    @PostMapping("/take-by-invite")
//    @Operation(summary = "基于邀请领取优惠劵")
//    @Parameter(name = "templateId", description = "优惠券模板编号", required = true, example = "1024")
//    public CommonResult<Boolean> takeCouponByInvite(@Valid @RequestBody AppCouponTakeByInviteReqVO reqVO) {
//        // 1. 领取优惠劵
//        Long userId = getLoginUserId();
//        couponService.takeCoupon(reqVO.getTemplateId(), CollUtil.newHashSet(userId), CouponTakeTypeEnum.USER);
//
//        // 2. 检查是否可以继续领取
//        CouponTemplateDO couponTemplate = couponTemplateService.getCouponTemplate(reqVO.getTemplateId());
//        boolean canTakeAgain = true;
//        if (couponTemplate.getTakeLimitCount() != null && couponTemplate.getTakeLimitCount() > 0) {
//            Integer takeCount = couponService.getTakeCount(reqVO.getTemplateId(), userId);
//            canTakeAgain = takeCount < couponTemplate.getTakeLimitCount();
//        }
//        return success(canTakeAgain);
//    }

    @GetMapping(value = "/get-unused-count")
    @Operation(summary = "获得未使用的优惠劵数量")
    public CommonResult<Long> getUnusedCouponCount() {
        return success(couponService.getUnusedCouponCount(getLoginUserId()));
    }


    @GetMapping("/page")
    @Operation(summary = "我的优惠劵列表")
    public CommonResult<PageResult<AppCouponRespVO>> getCouponPage(AppCouponPageReqVO pageReqVO) {
        PageResult<CouponDO> pageResult = couponService.getCouponPage(
                CouponConvert.INSTANCE.convert(pageReqVO, Collections.singleton(getLoginUserId())));
        return success(BeanUtils.toBean(pageResult, AppCouponRespVO.class));
    }
}
