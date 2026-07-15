package cn.iocoder.boot.module.product.api.sku;

import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.boot.module.product.dal.dataObject.sku.ProductSkuDO;
import cn.iocoder.boot.module.product.service.sku.ProductSkuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author xiaosheng
 */
@Service
public class ProductSkuApiImpl implements ProductSkuApi {
    @Resource
    private ProductSkuService productSkuService;
    @Override
    public List<ProductSkuRespDTO> getSkuList(Set<Long> ids) {
        List<ProductSkuDO> skus = productSkuService.getSkuList(ids);
        return BeanUtils.toBean(skus, ProductSkuRespDTO.class);
    }
}
