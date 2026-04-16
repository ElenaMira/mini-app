package cn.iocoder.boot.springsecurity.system.framework.sms.core.client.impl;

import cn.iocoder.boot.springsecurity.common.core.KeyValue;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClient;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 短信客户端的抽象类，提供模板方法，减少子类的冗余代码
 * @author xiaosheng
 */
@Slf4j
public abstract class AbstractSmsClient implements SmsClient {
    /**
     * 短信渠道配置
     * protected = 给子类用
     * volatile = 强制线程每次都读 “最新的值”，不读缓存里的旧值,保证多线程下，properties 的值 最新、可见、不脏读
     */
    protected volatile SmsChannelProperties properties;

    AbstractSmsClient(SmsChannelProperties properties){
        this.properties = properties;
    }

    @Override
    public Long getId() {
        return 0L;
    }

    /**
     * 短信渠道配置
     */

    /**
     * 初始化
     */
    public final void init() {
        log.debug("[init][配置({}) 初始化完成]", properties);
    }
    /**
     * 更新properties
     */
    public final void refresh(SmsChannelProperties properties){
        if(properties.equals(this.properties)){
            return;
        }
        //更新当前线程的properties
        log.info("[refresh][配置({})发生变化，重新初始化]", properties);
        this.properties = properties;
        // 初始化
        this.init();
    }
}
