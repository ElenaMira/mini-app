package cn.iocoder.boot.web.web.config;

import cn.iocoder.boot.web.web.core.util.WebFrameworkUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaosheng
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {
    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public WebFrameworkUtils  webFrameworkUtils(WebProperties webProperties){
        return new WebFrameworkUtils(webProperties);
    }
}
