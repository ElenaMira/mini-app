package cn.iocoder.boot.module.product.dal.mysql.spu;

import cn.iocoder.boot.module.product.dal.dataObject.spu.ProductSpuDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface ProductSpuMapper extends BaseMapperX<ProductSpuDO> {
    /**
     * 更新商品 SPU 浏览量
     *
     * @param id        商品 SPU 编号
     * @param incrCount 增加的数量
     */
    default void updateBrowseCount(Long id, int incrCount) {
        LambdaUpdateWrapper<ProductSpuDO> updateWrapper = new LambdaUpdateWrapper<ProductSpuDO>()
                .setSql(" browse_count = browse_count +" + incrCount)
                .eq(ProductSpuDO::getId, id);
        update(null, updateWrapper);
    }
}
