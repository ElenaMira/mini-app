package cn.iocoder.boot.module.system.service.sms;

import cn.iocoder.boot.module.system.dal.DO.sms.SmsChannelDO;
import cn.iocoder.boot.module.system.framework.sms.core.client.SmsClient;

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
