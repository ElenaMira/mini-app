package cn.iocoder.boot.module.promotion.dal.mysql.coupon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.common.validation.InEnum;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponTemplatePageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;
import cn.iocoder.boot.module.promotion.enums.coupon.CouponTemplateValidityTypeEnum;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiaosheng
 */
@Mapper
public interface CouponTemplateMapper extends BaseMapperX<CouponTemplateDO> {
    default PageResult<CouponTemplateDO> selectPage(CouponTemplatePageReqVO reqVO) {
        // 构建可领取的查询条件
        Consumer<LambdaQueryWrapper<CouponTemplateDO>> canTakeConsumer = buildCanTakeQueryConsumer(reqVO.getCanTakeTypes());

        return selectPage(reqVO, new LambdaQueryWrapperX<CouponTemplateDO>()
                .likeIfPresent(CouponTemplateDO::getName, reqVO.getName())
                .eqIfPresent(CouponTemplateDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CouponTemplateDO::getDiscountType, reqVO.getDiscountType())
                .betweenIfPresent(CouponTemplateDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CouponTemplateDO::getProductScope, reqVO.getProductScope())
                .and(reqVO.getProductScopeValue() != null, w -> w.apply("FIND_IN_SET({0}, product_scope_values)",
                        reqVO.getProductScopeValue()))
                .and(canTakeConsumer != null, canTakeConsumer)
                .orderByDesc(CouponTemplateDO::getId));

    }

    static Consumer<LambdaQueryWrapper<CouponTemplateDO>> buildCanTakeQueryConsumer(List<Integer> canTakeTypes) {
        Consumer<LambdaQueryWrapper<CouponTemplateDO>> canTakeConsumer = null;
        if (CollUtil.isNotEmpty(canTakeTypes)) {
            canTakeConsumer = w ->
                    w.eq(CouponTemplateDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                            .in(CouponTemplateDO::getTakeType, canTakeTypes)
                            .and(ww->ww.gt(CouponTemplateDO::getValidEndTime, LocalDateTime.now())
                                    .or().eq(CouponTemplateDO::getValidityType, CouponTemplateValidityTypeEnum.TERM.getType()))
                            .apply(" (take_count < total_count OR total_count = " + CouponTemplateDO.TOTAL_COUNT_MAX + ")");

        }
        return canTakeConsumer;
    }
}
