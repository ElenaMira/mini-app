package cn.iocoder.boot.springsecurity.mybatis.config;

import cn.iocoder.boot.springsecurity.mybatis.handle.DefaultDBFieldHandler;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaosheng
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
public class MybatisAutoConfiguration {
    @Bean
    public DefaultDBFieldHandler defaultMetaObjectHandler(){return new DefaultDBFieldHandler();}
}
