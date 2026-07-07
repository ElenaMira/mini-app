package cn.iocoder.boot.module.product.api.spu;

import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.boot.module.product.dal.dataObject.spu.ProductSpuDO;
import cn.iocoder.boot.module.product.service.spu.ProductSpuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaosheng
 */
@Service
public class ProductSpuApiImpl implements ProductSpuApi {
    @Resource
    private ProductSpuService productSpuService;

    @Override
    public List<ProductSpuRespDTO> getSpuList(Collection<Long> ids) {
        List<ProductSpuDO> spus = productSpuService.getSpuList(ids);
        return BeanUtils.toBean(spus, ProductSpuRespDTO.class);
    }

    @Override
    public ProductSpuRespDTO getSpu(Long spuId) {
        ProductSpuDO spu = productSpuService.getSpu(spuId);
        return BeanUtils.toBean(spu, ProductSpuRespDTO.class);
    }
}
