package cn.iocoder.boot.module.infra.framework.security.config;

import cn.iocoder.boot.springsecurity.config.AuthorizeRequestsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * @author xiaosheng
 *  Infra项目单独安全配置
 */
@Configuration(proxyBeanMethods = false, value = "infraSecurityConfiguration")
public class SecurityConfiguration {
    @Bean
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer(){
        return  new AuthorizeRequestsCustomizer() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                registry.requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll();

                // Spring Boot Actuator 的安全配置
                registry.requestMatchers("/actuator").permitAll()
                        .requestMatchers("/actuator/**").permitAll();
                // Druid 监控
                registry.requestMatchers("/druid/**").permitAll();
                //xxl-job
                registry.requestMatchers("xxl-job-admin/**").permitAll();
                registry.requestMatchers("/xxl-job-admin/**").permitAll();

                // 文件读取
                registry.requestMatchers(buildAdminApi("/infra/file/**")).permitAll();

            }
        };
    }
}
