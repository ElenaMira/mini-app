package cn.iocoder.boot.module.system.api.social;


import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserRespDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface SocialUserApi {
    /**
     *  绑定社交用户
     */
    String bindSocialUser(@Valid SocialUserBindReqDTO reqDTO);


    /**
     *  基于第三方code获取socialUser
     *
     *  在认证信息不正确的情况下，也会抛出 {@link ServiceException} 业务异常
     * @param userType
     * @param code
     * @param socialType
     * @param state
     * @return
     */
    SocialUserRespDTO getSocialUserByCode(Integer userType, String code, Integer socialType, String state);
}
