package cn.iocoder.boot.springsecurity.system.framework.sms.config;

import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.impl.SmsClientFactoryImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaosheng
 */
@Configuration
@EnableConfigurationProperties(SmsCodeProperties.class)
public class SmsCodeConfiguration {
    @Bean
    public SmsClientFactory smsClientFactory() {
        return new SmsClientFactoryImpl();
    }
}
