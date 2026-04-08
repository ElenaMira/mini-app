package cn.iocoder.boot.springsecurity.core.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.springsecurity.common.exception.ServiceException;
import cn.iocoder.boot.springsecurity.config.SecurityProperties;
import cn.iocoder.boot.springsecurity.core.LoginUser;
import cn.iocoder.boot.springsecurity.core.uitl.SecurityUtil;
import cn.iocoder.boot.springsecurity.core.uitl.WebFrameworkUtils;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.OAuth2TokenCommonApi;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author xiaosheng
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityProperties securityProperties;

    private final OAuth2TokenCommonApi oAuth2TokenCommonApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String token = SecurityUtil.obtainToken(request,securityProperties.getTokenHeader(),securityProperties.getTokenParameter());
            if(!StringUtils.isEmpty(token)){
                //1. 获取当前请求是【管理后台用户】还是【APP用户】
                Integer userType = WebFrameworkUtils.getLoginUserType(request);
                try{
                    //1.2 基于Token构建用户
                    LoginUser loginUser = buildLonginUserByToken(token, userType);
                    // 1.2 模拟 Login 功能，方便日常开发调试
                    if(loginUser == null){
                        loginUser = mockLoginUser(request, token, userType);
                    }

                    // 2. 设置当前用户
                    if (loginUser != null) {
                        SecurityUtil.setLoginUser(loginUser, request);
                    }
                }catch (Throwable e){
//                    CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
//                    ServletUtils.writeJSON(response, result);
//                    return;
                }
            }

            filterChain.doFilter(request,response);
    }

    /**
     *
     * @param token 携带的Token
     * @param userType 携带token的用户类型
     */
    private LoginUser buildLonginUserByToken(String token, Integer userType){
        try {
            OAuth2AccessTokenCheckRespDTO accessToken = oAuth2TokenCommonApi.checkAccessToken(token);
            if (accessToken == null) {
                return null;
            }
            if (userType != null && !ObjectUtil.equal(accessToken.getUserType(), userType)) {
                throw new AccessDeniedException("错误的用户类型");
            }
            return new LoginUser().setId(accessToken.getUserId()).setUserType(accessToken.getUserType())
                    .setInfo(accessToken.getUserInfo())
                    .setTenantId(accessToken.getTenantId())
                    .setScopes(accessToken.getScopes())
                    .setExpiresTime(accessToken.getExpiresTime());
        } catch (ServiceException e) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }
    /**
     * 模拟登录用户，方便日常开发调试
     *
     * 注意，在线上环境下，一定要关闭该功能！！！
     *
     * @param request 请求
     * @param token 模拟的 token，格式为 {@link SecurityProperties#getMockSecret()} + 用户编号
     * @param userType 用户类型
     * @return 模拟的 LoginUser
     */
    private LoginUser mockLoginUser(HttpServletRequest request, String token, Integer userType) {
        if (!securityProperties.getMockEnable()) {
            return null;
        }
        // 必须以 mockSecret 开头
        if (!token.startsWith(securityProperties.getMockSecret())) {
            return null;
        }
        // 构建模拟用户
        Long userId = Long.valueOf(token.substring(securityProperties.getMockSecret().length()));
        return new LoginUser().setId(userId).setUserType(userType);
    }
}
