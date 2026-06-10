package cn.iocoder.boot.common.util.servlet;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.iocoder.boot.common.util.json.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * @author xiaosheng
 * 解耦hutool的
 */
public class ServletUtils {


    @SuppressWarnings("deprecation")
    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JsonUtils.toJsonString(object);
        JakartaServletUtil.write(response, content, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

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
    /**
     * 获得请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(!(requestAttributes instanceof ServletRequestAttributes)){
            return null;
        }

        return  ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
