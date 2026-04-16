package cn.iocoder.boot.springsecurity.system.framework.sms.core.client.impl;

import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClient;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.enums.SmsChannelEnum;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiaosheng
 */
@Validated
@Slf4j
public class SmsClientFactoryImpl implements SmsClientFactory {

    /**
     * 短信客户端 Map
     * key：渠道编号，使用 {@link SmsChannelProperties#getId()}
     */
    private final ConcurrentMap<Long, AbstractSmsClient> channelIdClients = new ConcurrentHashMap<>();
    /**
     * 短信客户端 Map
     * key：渠道编码，使用 {@link SmsChannelProperties#getCode()} ()}
     *
     * 注意，一些场景下，需要获得某个渠道类型的客户端，所以需要使用它。
     * 例如说，解析短信接收结果，是相对通用的，不需要使用某个渠道编号的 {@link #channelIdClients}
     * 阿里云 / 腾讯云 推送短信回执、回复短信: 只告诉渠道类型：ALIYUN / TENCENT
     */
    private final ConcurrentMap<String,AbstractSmsClient> channelCodeClients = new ConcurrentHashMap<>();

    public SmsClientFactoryImpl(){
        Arrays.stream(SmsChannelEnum.values()).forEach(channel->{
            //创建空的SmsProperties
            SmsChannelProperties properties = new SmsChannelProperties()
                    .setCode(channel.getCode()).setApiKey("null")
                    .setApiSecret("null");
            //创建对应的客户端
            AbstractSmsClient client = createSmsClient(properties);
            channelCodeClients.put(channel.getCode(),client);
        });
    }

    @Override
    public SmsClient createOrUpdateSmsClient(SmsChannelProperties smsChannelProperties) {
        AbstractSmsClient client = channelIdClients.get(smsChannelProperties.getId());
        if(client==null){
            client=this.createSmsClient(smsChannelProperties);
            client.init();
            channelIdClients.put(smsChannelProperties.getId(),client);
        }else {
            client.refresh(smsChannelProperties);
        }
        return client;
    }

    private AbstractSmsClient createSmsClient(SmsChannelProperties properties){
        SmsChannelEnum channelEnum = SmsChannelEnum.getByCode(properties.getCode());
        Assert.notNull(channelEnum, String.format("渠道类型(%s) 为空", channelEnum));

        switch (channelEnum){
            case ALIYUN: return new AliyunSmsClient(properties);
//            case DEBUG_DING_TALK: return new DebugDingTalkSmsClient(properties);
//            case TENCENT: return new TencentSmsClient(properties);
//            case HUAWEI: return  new HuaweiSmsClient(properties);
//            case QINIU: return new QiniuSmsClient(properties);
        }
        // 创建失败，错误日志 + 抛出异常
        log.error("[createSmsClient][配置({}) 找不到合适的客户端实现]", properties);
        throw new IllegalArgumentException(String.format("配置(%s) 找不到合适的客户端实现", properties));
    }
}
