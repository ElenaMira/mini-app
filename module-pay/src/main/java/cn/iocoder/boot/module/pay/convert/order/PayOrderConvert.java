package cn.iocoder.boot.module.pay.convert.order;

import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.iocoder.boot.module.pay.api.order.PayOrderCreateReqDTO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderDO convert(PayOrderCreateReqDTO reqDTO);
}
