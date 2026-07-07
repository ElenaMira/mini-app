package cn.iocoder.boot.module.promotion.controller.app.coupon;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.product.api.spu.ProductSpuApi;
import cn.iocoder.boot.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponTemplatePageReqVO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.AppCouponTemplateRespVO;
import cn.iocoder.boot.module.promotion.convert.coupon.CouponTemplateConvert;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;
import cn.iocoder.boot.module.promotion.enums.common.PromotionProductScopeEnum;
import cn.iocoder.boot.module.promotion.enums.coupon.CouponTakeTypeEnum;
import cn.iocoder.boot.module.promotion.service.coupon.CouponService;
import cn.iocoder.boot.module.promotion.service.coupon.CouponTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;
import static java.util.Collections.singletonList;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 优惠劵模板")
@RestController
@RequestMapping("/promotion/coupon-template")
@Validated
public class AppCouponTemplateController {

    @Resource
    private ProductSpuApi productSpuApi;
    @Resource
    private CouponTemplateService couponTemplateService;
    @Resource
    private CouponService couponService;

    @GetMapping("/page")
    @Operation(summary = "获得优惠劵模版分页")
    @PermitAll
    public CommonResult<PageResult<AppCouponTemplateRespVO>> getCouponTemplatePage(AppCouponTemplatePageReqVO pageReqVO) {
        // 1.1 处理查询条件：商品范围编号
        Long productScopeValue = getProductScopeValue(pageReqVO.getProductScope(), pageReqVO.getSpuId());
        // 1.2 处理查询条件：领取方式 = 直接领取
        List<Integer> canTakeTypes = singletonList(CouponTakeTypeEnum.USER.getType());

        // 2. 分页查询
        PageResult<CouponTemplateDO> pageResult = couponTemplateService.getCouponTemplatePage(
                CouponTemplateConvert.INSTANCE.convert(pageReqVO, canTakeTypes, pageReqVO.getProductScope(), productScopeValue));

        // 3.1 领取数量
        Map<Long, Boolean> canCanTakeMap = couponService.getUserCanCanTakeMap(getLoginUserId(), pageResult.getList());
        // 3.2 拼接返回
        return success(CouponTemplateConvert.INSTANCE.convertAppPage(pageResult, canCanTakeMap));
    }

    /**
     * 获得商品的使用范围编号
     *
     * @param productScope 商品范围
     * @param spuId        商品 SPU 编号
     * @return 商品范围编号
     */
    private Long getProductScopeValue(Integer productScope, Long spuId) {
        // 通用券：没有商品范围
        if (ObjectUtils.equalsAny(productScope, PromotionProductScopeEnum.ALL.getScope(), null)) {
            return null;
        }
        // 品类券：查询商品的品类编号
        if (Objects.equals(productScope, PromotionProductScopeEnum.CATEGORY.getScope()) && spuId != null) {
            ProductSpuRespDTO spu = productSpuApi.getSpu(spuId);
            return spu != null ? spu.getCategoryId() : null;
        }
        // 商品劵：直接返回
        return spuId;
    }
}
