package cn.iocoder.boot.springsecurity.core.uitl;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.springsecurity.core.LoginUser;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 *  Spring Security使用的工具
 * @author 28235
 */
public class SecurityUtil {
    public static final  String AUTHORIZATION_BEARER = "bearer";
    /**
     *  获取请求头和请求体的Token
     */
    @Nullable
    public static String obtainToken(HttpServletRequest request,String headerToken,String parameterToken) {
        String token = request.getHeader(headerToken);
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter(parameterToken);
        }
        if (!StringUtils.hasText(token)) {
            return null;
        }
        int index = token.indexOf(AUTHORIZATION_BEARER+" ");
        //当index<0时,表明请求头只有token没有标头
        return index>=0?token.substring(7+index).trim():token;
    }


    @Nullable
    public static Long getLoginUserId(){
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getId();
    }

    @Nullable
    public static LoginUser getLoginUser(){
        Authentication authentication = getAuthentication();
        if(authentication == null){
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser)authentication.getPrincipal() : null;
    }
    public static void setLoginUser(LoginUser loginUser,HttpServletRequest request){
        Authentication authentication = buildAuthentication(loginUser, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(request != null){

        }
    }

    private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
        // 创建 UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, Collections.emptyList());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    public static Authentication getAuthentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }
}
