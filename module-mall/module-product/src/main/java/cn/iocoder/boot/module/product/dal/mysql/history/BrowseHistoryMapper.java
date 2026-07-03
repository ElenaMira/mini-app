package cn.iocoder.boot.module.product.dal.mysql.history;

import cn.iocoder.boot.module.product.dal.dataObject.history.ProductBrowseHistoryDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface BrowseHistoryMapper extends BaseMapperX<ProductBrowseHistoryDO> {
    default ProductBrowseHistoryDO selectByUserIdAndSpuId(Long loginUserId, Long spuId) {
        return selectOne(ProductBrowseHistoryDO::getUserId,loginUserId,ProductBrowseHistoryDO::getSpuId,spuId);
    }

    default Page<ProductBrowseHistoryDO> selectPageByUserIdOrderByCreateTimeAsc(Long loginUserId, Integer pageNo, Integer pageSize) {
        Page<ProductBrowseHistoryDO> page = Page.of(pageNo, pageSize);
        return selectPage(page, new LambdaQueryWrapperX<ProductBrowseHistoryDO>()
                .eqIfPresent(ProductBrowseHistoryDO::getUserId, loginUserId)
                .orderByAsc(ProductBrowseHistoryDO::getCreateTime));
    }

    default Long selectCountByUserId(Long loginUserId) {
        return selectCount(ProductBrowseHistoryDO::getUserId,loginUserId);
    }
}
