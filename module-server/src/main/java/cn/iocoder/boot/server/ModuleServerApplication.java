package cn.iocoder.boot.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */

@SpringBootApplication(scanBasePackages ={"${hd.info.base-package}.server", "${hd.info.base-package}.module"})
@MapperScan("cn.iocoder.boot.**.dal.mysql")

public class ModuleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleServerApplication.class, args);
    }
}
