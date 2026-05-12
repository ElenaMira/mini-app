package cn.iocoder.boot.mybatis.config;

import cn.iocoder.boot.mybatis.core.handle.DefaultDBFieldHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaosheng
 */
@AutoConfiguration
public class MybatisAutoConfiguration {
    @Bean
    public DefaultDBFieldHandler defaultMetaObjectHandler(){return new DefaultDBFieldHandler();}
}
