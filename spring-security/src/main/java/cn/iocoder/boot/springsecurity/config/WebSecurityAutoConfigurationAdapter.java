package cn.iocoder.boot.springsecurity.config;

import cn.iocoder.boot.springsecurity.core.filter.TokenAuthenticationFilter;
import cn.iocoder.boot.springsecurity.core.handle.AccessDeniedHandlerImpl;
import cn.iocoder.boot.springsecurity.core.handle.AuthenticationEntryPointImpl;
import com.google.common.collect.HashMultimap;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.google.common.collect.Multimap;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.boot.springsecurity.common.uitl.collection.CollectionUtils.convertList;

/**
 * @author xiaosheng
 */
@AutoConfiguration
@AutoConfigureOrder(-1)
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityAutoConfigurationAdapter {
    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 登出
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                //无状态放行
                .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(c->c.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        //
        Multimap<HttpMethod,String> permitAllUrls = getPermitAllUrlsFromAnnotations();
        httpSecurity
                // ①：全局共享规则
                .authorizeHttpRequests(c->c
                        //1. requestMatch
                        //1.1 静态资源，可匿名访问
                        .requestMatchers(HttpMethod.GET, "/*.html", "/*.css", "/*.js").permitAll()
                        //1.2 设置 @PermitAll 无需认证
                        .requestMatchers(HttpMethod.GET,permitAllUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.POST,permitAllUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.HEAD, permitAllUrls.get(HttpMethod.HEAD).toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.PATCH, permitAllUrls.get(HttpMethod.PATCH).toArray(new String[0])).permitAll()
                        //1.3 设置配置类无需认证
                        .requestMatchers(securityProperties.getPermitAllUrls().toArray(new String[0])).permitAll())
//               // ②：每个项目的自定义规则
//                .authorizeHttpRequests(c->c
//                        .requestMatchers()
//                )
                // ③：兜底规则，必须认证
                .authorizeHttpRequests(c -> c
//                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // WebFlux 异步请求，无需认证，目的：SSE 场景
                        .anyRequest().authenticated());
        httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    public Multimap<HttpMethod,String> getPermitAllUrlsFromAnnotations(){
        HashMultimap<HttpMethod, String> result = HashMultimap.create();
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        //遍历MVC的所有@PermitAll接口
        for(Map.Entry<RequestMappingInfo, HandlerMethod> entry: handlerMethods.entrySet()){
            HandlerMethod handlerMethod = entry.getValue();
            if(!handlerMethod.hasMethodAnnotation(PermitAll.class)
                &&!handlerMethod.getBeanType().isAnnotationPresent(PermitAll.class)){
                continue;
            }
            //获取接口上的URL
            Set<String> urls = new HashSet<>();
            if (entry.getKey().getPatternsCondition()!=null){
                urls.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition()!=null){
                urls.addAll(convertList(entry.getKey().getPathPatternsCondition().getPatterns()
                        , PathPattern::getPatternString));
            }
            if (urls.isEmpty()){
                continue;
            }


            //获取Method方法
            //1. 第一种情况: 接口未标明明确的方法,直接全部方法通过
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
            if(methods.isEmpty()){
                result.putAll(HttpMethod.GET,urls);
                result.putAll(HttpMethod.POST, urls);
                result.putAll(HttpMethod.PUT, urls);
                result.putAll(HttpMethod.DELETE, urls);
                result.putAll(HttpMethod.HEAD, urls);
                result.putAll(HttpMethod.PATCH, urls);
                continue;
            }

            //2. 有指定类型
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                    case HEAD:
                        result.putAll(HttpMethod.HEAD, urls);
                        break;
                    case PATCH:
                        result.putAll(HttpMethod.PATCH, urls);
                        break;
                }
            });
        }
        return result;
    }

}
