package cn.iocoder.boot.module.product.api.spu;

import cn.iocoder.boot.module.product.api.spu.dto.ProductSpuRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertMap;

/**
 * @author xiaosheng
 */
public interface ProductSpuApi {


    /**
     * 批量查询 SPU MAP
     *
     * @param ids SPU 编号列表
     * @return SPU MAP
     */
    default Map<Long, ProductSpuRespDTO> getSpuMap(Collection<Long> ids) {
        return convertMap(getSpuList(ids), ProductSpuRespDTO::getId);
    }
    /**
     * 批量查询 SPU 数组
     *
     * @param ids SPU 编号列表
     * @return SPU 数组
     */
    List<ProductSpuRespDTO> getSpuList(Collection<Long> ids);
}