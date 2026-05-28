package cn.iocoder.boot.module.product.service.category;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.product.dal.dataObject.category.ProductCategoryDO;
import cn.iocoder.boot.module.product.dal.mysql.ProductCategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaosheng
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategoryDO> getEnableCategoryList( ) {
        return productCategoryMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public List<ProductCategoryDO> getEnableCategoryList(List<Long> ids) {
        return productCategoryMapper.selectListByStatusAndId(ids,CommonStatusEnum.ENABLE.getStatus());
    }
}
