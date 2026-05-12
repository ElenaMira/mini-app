package cn.iocoder.boot.springsecurity.system.service.social;

import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserRespDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SocialUserService {

    String bindSocialUser(@Valid SocialUserBindReqDTO reqDTO);

    SocialUserRespDTO getSocialUserByCode(Integer userType, String code, Integer socialType, String state);
}
