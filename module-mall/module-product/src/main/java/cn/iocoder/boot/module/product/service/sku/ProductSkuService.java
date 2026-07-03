package cn.iocoder.boot.module.product.service.sku;

import cn.iocoder.boot.module.product.dal.dataObject.sku.ProductSkuDO;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface ProductSkuService {
    /**
     * 获得商品 SKU 集合
     *
     * @param spuId spu 编号
     * @return 商品sku 集合
     */
    List<ProductSkuDO> getSkuListBySpuId(Long spuId);
}
