package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsChannelDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.sms.SmsChannelMapper;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClient;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.property.SmsChannelProperties;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class SmsChannelServiceImpl implements SmsChannelService{
    @Resource
    private SmsChannelMapper smsChannelMapper;
    @Resource
    private SmsClientFactory smsClientFactory;
    @Override
//    @Cacheable(cacheNames = )
    public SmsChannelDO getChannel(Long id) {
        return smsChannelMapper.selectById(id);
    }

    @Override
    public SmsClient getSmsClient(Long id) {
        SmsChannelDO channelDO = smsChannelMapper.selectById(id);
        SmsChannelProperties channelProperties = BeanUtil.toBean(channelDO, SmsChannelProperties.class);
        return smsClientFactory.createOrUpdateSmsClient(channelProperties);
    }
}
