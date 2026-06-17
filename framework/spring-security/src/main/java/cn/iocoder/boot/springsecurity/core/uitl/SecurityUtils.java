package cn.iocoder.boot.springsecurity.core.uitl;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.springsecurity.config.SecurityProperties;
import cn.iocoder.boot.springsecurity.core.LoginUser;
import cn.iocoder.boot.web.web.core.util.WebFrameworkUtils;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import java.util.Collections;

import static cn.iocoder.boot.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;

/**
 *  Spring Security使用的工具
 * @author 28235
 */
public class SecurityUtils {
    public static final  String AUTHORIZATION_BEARER = "Bearer";
    /**
     *  获取请求头和请求体的Token
     *
     * @param request 请求
     * @param headerToken 认证 Token 对应的 Header 名字 {@link SecurityProperties}
     * @param parameterToken 认证 Token 对应的 Parameter 名字 {@link SecurityProperties}
     * @return 认证 Token
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

    /**
     * 业务逻辑上loginUser不会为null,支持匿名访问
     * @return loginUser
     */
    @NotNull
    public static Long getLoginUserId(){
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw exception(UNAUTHORIZED);
        }
        return loginUser.getId();
    }

    /**
     * 【匿名/可选登录接口专用】可选获取用户ID，无登录返回null
     */
    public static Long getLoginUserIdOrNull() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getId();
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
            WebFrameworkUtils.setLoginUserId(request,loginUser.getId());
            WebFrameworkUtils.setLoginUserType(request, loginUser.getUserType());
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
