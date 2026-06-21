package cn.iocoder.boot.module.pay.framework.pay.core.client;

import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;

public interface PayClient<Config> {

    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();

    /**
     * 获得渠道配置
     *
     * @return 渠道配置
     */
    Config getConfig();

    /**
     * 获得支付订单信息
     * @param no    订单编号
     * @return  支付订单信息
     */
    PayOrderRespDTO getOrder(String no);
}
