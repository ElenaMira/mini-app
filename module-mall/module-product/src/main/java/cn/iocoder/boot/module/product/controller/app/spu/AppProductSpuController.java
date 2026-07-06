package cn.iocoder.boot.module.product.controller.app.spu;

import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.product.controller.app.spu.vo.AppProductSpuDetailRespVO;
import cn.iocoder.boot.module.product.dal.dataObject.sku.ProductSkuDO;
import cn.iocoder.boot.module.product.dal.dataObject.spu.ProductSpuDO;
import cn.iocoder.boot.module.product.enums.spu.ProductSpuStatusEnum;
import cn.iocoder.boot.module.product.service.history.ProductBrowseHistoryService;
import cn.iocoder.boot.module.product.service.sku.ProductSkuService;
import cn.iocoder.boot.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.module.product.enums.ErrorCodeConstants.SPU_NOT_ENABLE;
import static cn.iocoder.boot.module.product.enums.ErrorCodeConstants.SPU_NOT_EXISTS;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 商品 SPU")
@RestController
@RequestMapping("/product/spu")
@Validated
public class AppProductSpuController {
    @Resource
    private ProductSpuService productSpuService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductBrowseHistoryService productBrowseHistoryService;


    @GetMapping("/get-detail")
    @Operation(summary = "获得商品 SPU 明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PermitAll
    public CommonResult<AppProductSpuDetailRespVO> getSpuDetail(@RequestParam("id") Long id) {
        // 获得商品 SPU
        ProductSpuDO spu = productSpuService.getSpu(id);
        if (spu == null) {
            throw exception(SPU_NOT_EXISTS);
        }
        if (!ProductSpuStatusEnum.isEnable(spu.getStatus())) {
            throw exception(SPU_NOT_ENABLE, spu.getName());
        }
        // 获得商品 SKU
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spu.getId());

        // 增加浏览量
        productSpuService.updateBrowseCount(id, 1);
        // 保存浏览记录
        productBrowseHistoryService.createBrowseHistory(getLoginUserId(), id);

        // 拼接返回
        spu.setSalesCount(spu.getSalesCount() + spu.getVirtualSalesCount());
        AppProductSpuDetailRespVO spuVO = BeanUtils.toBean(spu, AppProductSpuDetailRespVO.class)
                .setSkus(BeanUtils.toBean(skus, AppProductSpuDetailRespVO.Sku.class));
        return success(spuVO);
    }

}
