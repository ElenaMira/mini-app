package cn.iocoder.boot.module.promotion.controller.app.coupon;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.promotion.service.coupon.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/get-unused-count")
    @Operation(summary = "获得未使用的优惠劵数量")
    public CommonResult<Long> getUnusedCouponCount() {
        return success(couponService.getUnusedCouponCount(getLoginUserId()));
    }
}
