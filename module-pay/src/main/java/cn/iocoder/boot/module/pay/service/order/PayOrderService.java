package cn.iocoder.boot.module.pay.service.order;

import cn.iocoder.boot.module.pay.api.order.PayOrderCreateReqDTO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface PayOrderService {
    /**
     * 获得支付订单
     *
     * @param id 编号
     * @return 支付订单
     */
    PayOrderDO getOrder(Long id);

    /**
     * 获得支付订单
     *
     * @param no 支付订单号
     * @return 支付订单
     */
    PayOrderDO getOrder(String no);

    /**
     * 同步订单的支付状态
     * @param orderId 订单ID
     */
    void syncOrderQuietly(Long orderId);

    /**
     * 通知支付单成功
     * @param channelId 渠道ID
     * @param notify    通知信息
     */
    void notifyOrder(Long channelId, PayOrderRespDTO notify);

    /**
     * 创建订单
     * @param reqDTO
     * @return
     */
    Long createOrder(@Valid PayOrderCreateReqDTO reqDTO);
}
