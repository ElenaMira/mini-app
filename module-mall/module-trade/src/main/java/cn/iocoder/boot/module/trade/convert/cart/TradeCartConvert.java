package cn.iocoder.boot.module.trade.convert.cart;

import cn.iocoder.boot.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.boot.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.boot.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.boot.module.trade.dal.dataobject.cart.CartDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface TradeCartConvert {
    TradeCartConvert INSTANCE = Mappers.getMapper(TradeCartConvert.class);

    AppCartListRespVO convertList(List<CartDO> cartDOList, List<ProductSpuRespDTO> spuList, List<ProductSkuRespDTO> skuList);
}
