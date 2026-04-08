package cn.iocoder.boot.springsecurity.config;


import cn.iocoder.boot.springsecurity.core.filter.TokenAuthenticationFilter;
import cn.iocoder.boot.springsecurity.core.handle.AccessDeniedHandlerImpl;
import cn.iocoder.boot.springsecurity.core.handle.AuthenticationEntryPointImpl;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.OAuth2TokenCommonApi;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author xiaosheng
 */
@AutoConfiguration
@AutoConfigureOrder(-1)
@EnableConfigurationProperties({SecurityProperties.class,WebProperties.class})
public class SecurityAutoConfiguration {
    @Resource
    private SecurityProperties securityProperties;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){return new AuthenticationEntryPointImpl();}

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){return new AccessDeniedHandlerImpl();}


    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());}

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(OAuth2TokenCommonApi oAuth2TokenCommonApi){
        return new TokenAuthenticationFilter(securityProperties,oAuth2TokenCommonApi);
    }


}
