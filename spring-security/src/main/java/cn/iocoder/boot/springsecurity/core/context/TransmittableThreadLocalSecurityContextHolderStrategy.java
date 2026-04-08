package cn.iocoder.boot.springsecurity.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

/**
 *  用阿里的TransmittableThreadLocal 实现的 Security Context 持有者策略
 *  目的是，避免 @Async 等异步执行时，原生 ThreadLocal 的丢失问题
 */

public class TransmittableThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    /**
     * 使用TransmittableThreadLocal作为上下文存储SECURITY_CONTEXT
     */
    private static final ThreadLocal<SecurityContext> SECURITY_CONTEXT = new TransmittableThreadLocal<>();

    @Override
    public void clearContext() {
        SECURITY_CONTEXT.remove();
    }

    @Override
    public SecurityContext getContext() {
        SecurityContext context = SECURITY_CONTEXT.get();
        // 内容可以为空,但是不能为null
        if(context == null) {
            context = SecurityContextHolder.createEmptyContext();
            SECURITY_CONTEXT.set(context);
        }
        return context;
    }

    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        SECURITY_CONTEXT.set(context);
    }


    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
