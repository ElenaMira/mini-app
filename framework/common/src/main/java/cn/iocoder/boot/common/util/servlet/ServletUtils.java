package cn.iocoder.boot.common.util.servlet;

import cn.hutool.extra.servlet.JakartaServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author xiaosheng
 * 解耦hutool的
 */
public class ServletUtils {
    /**
     *  使用hutool专门给jdk17的JakartaServletUtil获取IP
     * @return
     */
    public static String getClientIP(){
        HttpServletRequest request = getRequest();
        if (request==null){
            return null;
        }
        return JakartaServletUtil.getClientIP(request);
    }

    private static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(!(requestAttributes instanceof ServletRequestAttributes)){
            return null;
        }

        return  ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
