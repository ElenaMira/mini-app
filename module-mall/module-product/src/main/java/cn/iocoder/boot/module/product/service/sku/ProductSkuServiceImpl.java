package cn.iocoder.boot.module.product.service.sku;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.iocoder.boot.module.product.dal.dataObject.sku.ProductSkuDO;
import cn.iocoder.boot.module.product.dal.mysql.sku.ProductSkuMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaosheng
 */
@Service
public class ProductSkuServiceImpl implements ProductSkuService {
    @Resource
    private ProductSkuMapper productSkuMapper;

    @Override
    public List<ProductSkuDO> getSkuListBySpuId(Long spuId) {
        return productSkuMapper.selectListBySpuId(spuId);
    }

    @Override
    public List<ProductSkuDO> getSkuList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return productSkuMapper.selectByIds(ids);
    }
}
