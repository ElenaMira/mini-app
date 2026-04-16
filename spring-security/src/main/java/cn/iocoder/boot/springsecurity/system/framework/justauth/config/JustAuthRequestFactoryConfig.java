package cn.iocoder.boot.springsecurity.system.framework.justauth.config;

import cn.iocoder.boot.springsecurity.system.framework.justauth.core.AuthRequestFactory;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import com.xkcoding.justauth.support.cache.RedisStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author xiaosheng
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JustAuthProperties.class) //手动注册老版本justAuth-start自动装配类
public class JustAuthRequestFactoryConfig {
    @Bean
//    @ConditionalOnBean(JustAuthProperties.class) //手动将这个当前Bean注册在JustAuthProperties之后
    @ConditionalOnProperty(
            prefix = "justauth",
            value = {"enabled"},
            havingValue = "true",
            matchIfMissing = true
    )//基于条件创建当前Bean
    public AuthRequestFactory getAuRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache){
        return new AuthRequestFactory(properties,authStateCache);
    }
    @Bean
//    @ConditionalOnBean(JustAuthProperties.class)
    public AuthStateCache authStateCache(RedisTemplate<String, String> justAuthRedisCacheTemplate,
                                          JustAuthProperties properties) {
        return new RedisStateCache(justAuthRedisCacheTemplate, properties.getCache());
    }
}
