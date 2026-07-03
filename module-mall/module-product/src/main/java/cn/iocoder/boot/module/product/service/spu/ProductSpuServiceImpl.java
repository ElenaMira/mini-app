package cn.iocoder.boot.module.product.service.spu;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.module.product.dal.dataObject.spu.ProductSpuDO;
import cn.iocoder.boot.module.product.dal.mysql.spu.ProductSpuMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertMap;


/**
 * @author xiaosheng
 */
@Service
public class ProductSpuServiceImpl implements ProductSpuService {
    @Resource
    private ProductSpuMapper productSpuMapper;
    @Override
    public List<ProductSpuDO> getSpuList(Collection<Long> ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        Map<Long, ProductSpuDO> spuMap = convertMap(productSpuMapper.selectByIds(ids), ProductSpuDO::getId);
        return convertList(ids, spuMap::get);
    }

    @Override
    public ProductSpuDO getSpu(Long id) {
        return productSpuMapper.selectById(id);
    }

    @Override
    public void updateBrowseCount(Long userId, int incrCount) {
        productSpuMapper.updateBrowseCount(userId , incrCount);
    }
}
