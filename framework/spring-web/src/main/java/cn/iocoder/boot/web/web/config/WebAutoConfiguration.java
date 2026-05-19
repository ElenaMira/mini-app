package cn.iocoder.boot.web.web.config;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.web.web.core.util.WebFrameworkUtils;
import com.google.common.collect.Maps;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author xiaosheng
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    @Bean
    public WebMvcRegistrations webMvcRegistrations(WebProperties webProperties){
        return new WebMvcRegistrations() {
            @Override
            public  RequestMappingHandlerMapping getRequestMappingHandlerMapping(){
                RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
                //实例化带上前缀
                requestMappingHandlerMapping.setPathPrefixes(buildPathPrefixes(webProperties));
                return requestMappingHandlerMapping;
            }
            /**
             * 构建 prefix → 匹配条件的映射
             */
            private Map<String, Predicate<Class<?>>> buildPathPrefixes(WebProperties webProperties){
                AntPathMatcher antPathMatcher = new AntPathMatcher(".");
                Map<String,  Predicate<Class<?>>> pathPrefixes = Maps.newLinkedHashMapWithExpectedSize(2);
                putPathPrefix(pathPrefixes,webProperties.getAppApi(),antPathMatcher);
                putPathPrefix(pathPrefixes,webProperties.getAdminApi(),antPathMatcher);
                return pathPrefixes;
            }
            /**
             * 设置 API 前缀，仅仅匹配 controller 包下的
             */
            private void putPathPrefix(Map<String,  Predicate<Class<?>>> pathPrefixes,WebProperties.Api api,
                                       AntPathMatcher matcher){
                if (api==null|| StrUtil.isEmpty(api.getPrefix())){
                    return;
                }
                pathPrefixes.put(api.getPrefix(), // api 前缀
                        clazz -> clazz.isAnnotationPresent(RestController.class)
                        &&matcher.match(api.getController(),clazz.getPackage().getName()));
            }
        };
    }

    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public WebFrameworkUtils  webFrameworkUtils(WebProperties webProperties){
        return new WebFrameworkUtils(webProperties);
    }
}
