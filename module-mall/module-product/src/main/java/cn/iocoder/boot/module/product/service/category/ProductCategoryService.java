package cn.iocoder.boot.module.product.service.category;

import cn.iocoder.boot.module.product.dal.dataObject.category.ProductCategoryDO;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface ProductCategoryService {
    /**
     *  获得开启状态的商品分类列表
     * @return 商品分类列表
     */

    List<ProductCategoryDO> getEnableCategoryList();

    /**
     * 获得开启状态的商品分类列表，指定编号
     *
     * @param ids 商品分类编号数组
     * @return 商品分类列表
     */
    List<ProductCategoryDO> getEnableCategoryList(List<Long> ids);
}
