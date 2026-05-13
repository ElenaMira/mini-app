package cn.iocoder.boot.web.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author xiaosheng
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {
}
