package cn.iocoder.boot.module.product.api.sku;

import cn.iocoder.boot.module.product.api.sku.dto.ProductSkuRespDTO;

import java.util.List;
import java.util.Set;

/**
 * @author xiaosheng
 */
public interface ProductSkuApi {
    List<ProductSkuRespDTO> getSkuList(Set<Long> ids);
}
