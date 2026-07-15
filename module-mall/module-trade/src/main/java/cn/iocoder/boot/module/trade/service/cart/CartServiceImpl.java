package cn.iocoder.boot.module.trade.service.cart;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.boot.module.product.api.sku.ProductSkuApi;
import cn.iocoder.boot.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.boot.module.product.api.spu.ProductSpuApi;
import cn.iocoder.boot.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.boot.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.boot.module.trade.convert.cart.TradeCartConvert;
import cn.iocoder.boot.module.trade.dal.dataobject.cart.CartDO;
import cn.iocoder.boot.module.trade.dal.mysql.cart.CartMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertSet;
import static java.util.Collections.emptyList;

/**
 * @author xiaosheng
 */
@Service
public class CartServiceImpl implements CartService {
    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductSpuApi productSpuApi;
    @Resource
    private ProductSkuApi productSkuApi;

    @Override
    public AppCartListRespVO getCartList(Long loginUserId) {
        //获取购物车列表
        List<CartDO> cartDOList = cartMapper.selectListByUserId(loginUserId);
        cartDOList.sort(Comparator.comparing(CartDO::getId).reversed());
        // 如果为空，则返回空结果
        if (CollUtil.isEmpty(cartDOList)) {
            return new AppCartListRespVO().setValidList(emptyList())
                    .setInvalidList(emptyList());
        }
        // 查询 SPU、SKU 列表
        List<ProductSpuRespDTO> spuList = productSpuApi.getSpuList(convertSet(cartDOList, CartDO::getSpuId));
        List<ProductSkuRespDTO> skuList = productSkuApi.getSkuList(convertSet(cartDOList, CartDO::getSkuId));

        deleteCartIfSpuDeleted(cartDOList, spuList);

        // 拼接数据
        return TradeCartConvert.INSTANCE.convertList(cartDOList, spuList, skuList);
    }

    private void deleteCartIfSpuDeleted(List<CartDO> cartDOList, List<ProductSpuRespDTO> spuList) {
        // 1. 收集需要删除的购物车ID
        List<Long> deleteCartIds  = cartDOList.stream()
                .filter(cart -> spuList.stream().noneMatch(spu -> spu.getId().equals(cart.getSpuId())))
                .map(CartDO::getId)
                .toList();
        // 2. 批量数据库删除，减少SQL次数
        if (CollUtil.isNotEmpty(deleteCartIds)) {
            cartMapper.deleteBatchIds(deleteCartIds);
            // 3. 内存过滤，移除已删除条目
            cartDOList.removeIf(cart -> deleteCartIds.contains(cart.getId()));
        }
    }
}
