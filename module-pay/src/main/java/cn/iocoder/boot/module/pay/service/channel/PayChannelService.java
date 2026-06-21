package cn.iocoder.boot.module.pay.service.channel;

import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;

/**
 * @author xiaosheng
 */
public interface PayChannelService {

    /**
     * 支付渠道的合法性
     *
     * 如果不合法，抛出 {@link ServiceException} 业务异常
     *
     * @param id 渠道编号
     * @return 渠道信息
     */
    PayChannelDO validPayChannel(Long id);

    /**
     * 支付渠道的合法性
     *
     * 如果不合法，抛出 {@link ServiceException} 业务异常
     *
     * @param appId 应用编号
     * @param code 支付渠道
     * @return 渠道信息
     */
    PayChannelDO validPayChannel(Long appId, String code);
    /**
     * 获得指定编号的支付客户端
     *
     * @param channelId 编号
     * @return 支付客户端
     */
    PayClient getPayClient(Long channelId);
}
