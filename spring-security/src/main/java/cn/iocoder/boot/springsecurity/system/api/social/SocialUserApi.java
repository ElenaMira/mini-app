package cn.iocoder.boot.springsecurity.system.api.social;

import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SocialUserApi {
    /**
     *  绑定社交用户
     */
    String bindSocialUser(@Valid SocialUserBindReqDTO reqDTO);
}
