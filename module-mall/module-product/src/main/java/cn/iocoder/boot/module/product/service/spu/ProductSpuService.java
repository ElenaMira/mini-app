package cn.iocoder.boot.module.product.service.spu;

import cn.iocoder.boot.module.product.dal.dataObject.spu.ProductSpuDO;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaosheng
 */
public interface ProductSpuService {
    /**
     * 基于Ids查询Spu
     * @param ids   SpuId
     * @return
     */
    List<ProductSpuDO> getSpuList(Collection<Long> ids);

    /**
     * 获得商品 SPU
     *
     * @param id 编号
     * @return 商品 SPU
     */
    ProductSpuDO getSpu(Long id);

    /**
     * 更新商品 SPU 浏览量
     *
     * @param userId        商品 SPU 编号
     * @param incrCount 增加的数量
     */
    @Async
    void updateBrowseCount(Long userId, int incrCount);
}
