package cn.iocoder.boot.module.pay.convert.order;

import cn.iocoder.boot.module.pay.api.order.PayOrderCreateReqDTO;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderSubmitRespVO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderUnifiedReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderDO convert(PayOrderCreateReqDTO reqDTO);

    PayOrderExtensionDO convert(AppPayOrderSubmitReqVO reqDTO, String userIp);

    PayOrderUnifiedReqDTO convert2(AppPayOrderSubmitReqVO reqVO, String userIp);

    @Mapping(source = "order.status", target = "status")
    AppPayOrderSubmitRespVO convert(PayOrderDO order, PayOrderRespDTO respDTO);

}
