package cn.iocoder.boot.module.promotion.controller.app.point;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.product.api.spu.ProductSpuApi;
import cn.iocoder.boot.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.boot.module.product.dal.dataObject.spu.ProductSpuDO;
import cn.iocoder.boot.module.promotion.controller.app.point.vo.AppPointActivityPageReqVO;
import cn.iocoder.boot.module.promotion.controller.app.point.vo.AppPointActivityRespVO;
import cn.iocoder.boot.module.promotion.controller.app.point.vo.PointActivityPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointActivityDO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointProductDO;
import cn.iocoder.boot.module.promotion.service.point.PointActivityService;
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

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.common.util.collection.CollectionUtils.*;
import static cn.iocoder.boot.common.util.collection.MapUtils.findAndThen;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 积分商城活动")
@RestController
@RequestMapping("/promotion/point-activity")
@Validated
public class AppPointActivityController {

    @Resource
    private PointActivityService pointActivityService;
    @Resource
    private ProductSpuApi productSpuApi;

    @GetMapping("/page")
    @Operation(summary = "获得积分商城活动分页")
    @PermitAll
    public CommonResult<PageResult<AppPointActivityRespVO>> getPointActivityPage(AppPointActivityPageReqVO pageReqVO) {
        // 1. 查询满足当前阶段的活动
        PageResult<PointActivityDO> pageResult = pointActivityService.getPointActivityPage(
                BeanUtils.toBean(pageReqVO,PointActivityPageReqVO.class));
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty(pageResult.getTotal()));
        }

        // 2. 拼接数据
        List<AppPointActivityRespVO> resultList = buildAppPointActivityRespVOList(pageResult.getList());
        return success(new PageResult<>(resultList, pageResult.getTotal()));
    }

    private List<AppPointActivityRespVO> buildAppPointActivityRespVOList(List<PointActivityDO> list) {
        List<PointProductDO> products = pointActivityService.getPointProductListByActivityIds(
                convertSet(list, PointActivityDO::getId));
        Map<Long, List<PointProductDO>> productsMap = convertMultiMap(products, PointProductDO::getActivityId);
        Map<Long, ProductSpuRespDTO>  spuMap = productSpuApi.getSpuMap(convertSet(products, PointProductDO::getSpuId));
        List<AppPointActivityRespVO> result = BeanUtils.toBean(list, AppPointActivityRespVO.class);
        result.forEach(respVO -> {
            PointProductDO minProduct = getMinObject(productsMap.get(respVO.getId()), PointProductDO::getPoint);
            if (null==minProduct) {
                respVO.setPoint(null);
                return;
            }
            respVO.setPoint(minProduct.getPoint());
            findAndThen(spuMap,minProduct.getSpuId(),
                    spu-> respVO.setSpuName(spu.getName()).setPicUrl(spu.getPicUrl()).setMarketPrice(spu.getMarketPrice()));
        });
        return result;
    }
}
