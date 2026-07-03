package cn.iocoder.boot.job.config;


import org.springframework.beans.BeansException;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author xiaosheng
 * 异步任务自动配置
 * 全局统一给所有异步线程池注入TTL装饰器，解决@Async丢失ThreadLocal上下文（traceId/登录用户/MDC日志）
 */
@AutoConfiguration
@EnableAsync
public class AsyncAutoConfiguration {

    /**
     * 全局Bean后置处理器，拦截所有Spring异步线程池，统一配置TTL上下文传递装饰器
     */
    @Bean
    public BeanPostProcessor threadPoolTaskExecutorBeanPostProcessor(){
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean,String beanName) throws BeansException {
                //  处理自定义 ThreadPoolTaskExecutor
                if (bean instanceof ThreadPoolTaskExecutor executor) {
                    //线程池所有异步任务自动走 TTL 包装
                    executor.setTaskDecorator(TtlRunnable::get);
                    return executor;
                }
                //  处理默认线程池 SimpleAsyncTaskExecutor
                if (bean instanceof SimpleAsyncTaskExecutor executor){
                    executor.setTaskDecorator(TtlRunnable::get);
                    return executor;
                }
                return bean;
            }
        };
    }
}
