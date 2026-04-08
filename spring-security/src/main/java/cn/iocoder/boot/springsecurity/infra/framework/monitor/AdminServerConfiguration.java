//package cn.iocoder.boot.springsecurity.infra.framework.monitor;
//
//import de.codecentric.boot.admin.server.config.EnableAdminServer;
//import jakarta.servlet.DispatcherType;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//
///**
// * @author xiaosheng
// */
//@Configuration(proxyBeanMethods = false)
//@EnableAdminServer
//@ConditionalOnClass(name = "de.codecentric.boot.admin.server.config.AdminServerProperties")
//public class AdminServerConfiguration {
//    @Value("${spring.boot.admin.context-path:''}")
//    private String adminSeverContextPath;
//
//    @Value("${spring.boot.admin.client.username:admin}")
//    private String username;
//
//    @Value("${spring.boot.admin.client.password:admin}")
//    private String password;
//
//    @Bean("adminUserDetailsManager")
//    public InMemoryUserDetailsManager adminUserDetailsManager(PasswordEncoder passwordEncoder) {
//        UserDetails adminUser = User.builder()
//                .username(username)
//                .password(passwordEncoder.encode(password))
//                .roles("ADMIN_SERVER")
//                .build();
//        return new InMemoryUserDetailsManager(adminUser);
//    }
//
//    @Bean("adminServerSecurityFilterChain")
//    @Order(1)
//    public SecurityFilterChain adminServerSecurityFilterChain(HttpSecurity httpSecurity,
//                                                              InMemoryUserDetailsManager adminUserDetailsManager) throws Exception {
//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        httpSecurity
//                // 仅匹配 Admin Server 的路径
//                .securityMatcher(adminSeverContextPath + "/**")
//                // 使用独立的 UserDetailsManager
//                .userDetailsService(adminUserDetailsManager)
//                //
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(adminSeverContextPath + "/assets/**").permitAll() // 静态资源允许匿名访问
//                        .requestMatchers(adminSeverContextPath + "/login").permitAll() // 登录页面允许匿名访问
//                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // 异步请求允许
//                        .anyRequest().authenticated() // 其他请求需要认证
//                )
////                .formLogin(form->form
////                        .loginPage(adminSeverContextPath + "/login")
////                        .successHandler(successHandler)
////                        .permitAll()
//                .formLogin(Customizer.withDefaults())
//                .logout(c->c
//                        .logoutUrl(adminSeverContextPath+"/logout")
//                        .logoutSuccessUrl("/login")
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
//                .csrf(AbstractHttpConfigurer::disable);
//        ;
//        return httpSecurity.build();
//    }
//}
