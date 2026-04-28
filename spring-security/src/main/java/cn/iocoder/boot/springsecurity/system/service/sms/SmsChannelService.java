package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsChannelDO;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClient;

/**
 * @author xiaosheng
 */
public interface SmsChannelService {
    SmsChannelDO getChannel(Long channelId);

    /**
     *
     * @param channelId channel表Id
     * @return
     */
    SmsClient getSmsClient(Long channelId);
}
