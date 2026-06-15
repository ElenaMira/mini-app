package cn.iocoder.boot.module.infra.framework.file.config;

import cn.iocoder.boot.module.infra.framework.file.core.client.FileClientFactory;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author xiaosheng
 */
@AutoConfiguration
public class FileAutoConfiguration {
    @Bean
    public FileClientFactory fileClientFactory(){return new FileFactoryImpl();
    }
}
