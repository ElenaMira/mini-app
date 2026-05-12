package cn.iocoder.boot.springsecurity.system.api.social;

import cn.iocoder.boot.springsecurity.common.validation.InEnum;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserRespDTO;
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
     * @param userType
     * @param code
     * @param socialType
     * @param state
     * @return
     */
    SocialUserRespDTO getSocialUserByCode(Integer userType, String code, Integer socialType, String state);
}
