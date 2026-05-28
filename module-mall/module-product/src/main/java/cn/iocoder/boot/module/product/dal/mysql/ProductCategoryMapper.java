package cn.iocoder.boot.module.product.dal.mysql;

import cn.iocoder.boot.module.product.dal.dataObject.category.ProductCategoryDO;
import cn.iocoder.boot.module.product.service.category.ProductCategoryService;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapperX<ProductCategoryDO> {


    default List<ProductCategoryDO> selectListByStatus(Integer status){
        return selectList(new LambdaQueryWrapperX<ProductCategoryDO>()
                .eq(ProductCategoryDO::getStatus, status));
    }

    default List<ProductCategoryDO> selectListByStatusAndId(List<Long> ids, Integer status) {
        return selectList(new LambdaQueryWrapperX<ProductCategoryDO>()
                .in(ProductCategoryDO::getStatus, ids)
                .eq(ProductCategoryDO::getStatus, status));
    }
}
