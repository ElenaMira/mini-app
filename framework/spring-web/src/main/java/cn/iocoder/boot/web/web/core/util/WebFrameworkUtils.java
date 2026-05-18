package cn.iocoder.boot.web.web.core.util;

import cn.hutool.core.util.NumberUtil;
import cn.iocoder.boot.common.enums.TerminalEnum;
import cn.iocoder.boot.common.enums.UserTypeEnum;

import cn.iocoder.boot.web.web.config.WebProperties;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author xiaosheng
 */
public class WebFrameworkUtils {
    public static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";

    private static WebProperties webProperties;


    public static final String HEADER_TERMINAL = "terminal";

    public WebFrameworkUtils(WebProperties webProperties) {
        WebFrameworkUtils.webProperties = webProperties;
    }

    /**
     *
     * @return usertype
     */
    @Nullable
    public static Integer getLoginUserType(HttpServletRequest request) {
        if(request==null){
            return null;
        }
        // 从Attribute获取
        Integer userType = (Integer) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE);
        if(userType!=null){
            return userType;
        }
        // 从url中取
        if(request.getServletPath().startsWith(webProperties.getAdminApi().getPrefix())){
            return UserTypeEnum.ADMIN.getValue();
        }
        if(request.getServletPath().startsWith(webProperties.getAppApi().getPrefix())){
            return UserTypeEnum.MEMBER.getValue();
        }
        return null;
    }

    public static Integer getTerminal() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return TerminalEnum.UNKNOWN.getTerminal();
        }
        String terminalValue = request.getHeader(HEADER_TERMINAL);
        return NumberUtil.parseInt(terminalValue, TerminalEnum.UNKNOWN.getTerminal());
    }
    @SuppressWarnings("PatternVariableCanBeUsed")
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

    public static void setLoginUserId(HttpServletRequest request, Long userId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, userId);
    }

    public static void setLoginUserType(HttpServletRequest request, Integer userType) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE,userType);
    }
}
