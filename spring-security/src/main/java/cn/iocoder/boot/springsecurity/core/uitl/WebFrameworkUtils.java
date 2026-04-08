package cn.iocoder.boot.springsecurity.core.uitl;

import cn.iocoder.boot.springsecurity.config.WebProperties;
import cn.iocoder.boot.springsecurity.common.enums.UserTypeEnum;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author xiaosheng
 */
public class WebFrameworkUtils {
    public static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";

    private static WebProperties webProperties;

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
}
