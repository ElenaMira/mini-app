package cn.iocoder.boot.module.pay.service.channel;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.boot.module.pay.dal.mysql.channel.PayChannelMapper;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientFactory;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.module.pay.enums.ErrorCodeConstants.CHANNEL_IS_DISABLE;
import static cn.iocoder.boot.module.pay.enums.ErrorCodeConstants.CHANNEL_NOT_FOUND;

/**
 * @author xiaosheng
 */
@Service
public class PayChannelServiceImpl implements PayChannelService {
    @Resource
    private PayChannelMapper payChannelMapper;
    @Autowired
    private PayClientFactory payClientFactory;

    @Override
    public PayChannelDO validPayChannel(Long id) {
        PayChannelDO payChannelDO = payChannelMapper.selectById(id);
        validPayChannel(payChannelDO);
        return payChannelDO;
    }


    @Override
    public PayChannelDO validPayChannel(Long appId, String code) {
        PayChannelDO payChannelDO = payChannelMapper.selectByAppIdAndCode(appId, code);
        validPayChannel(payChannelDO);
        return payChannelDO;
    }



    @Override
    public PayClient getPayClient(Long channelId) {
        PayChannelDO channel = validPayChannel(channelId);
        return payClientFactory.createOrUpdatePayClient(channelId,channel.getCode(),channel.getConfig());
    }


    private void validPayChannel(PayChannelDO payChannelDO) {
        if (null == payChannelDO) {
            throw exception(CHANNEL_NOT_FOUND);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(payChannelDO.getStatus())) {
            throw exception(CHANNEL_IS_DISABLE);
        }
    }
}
