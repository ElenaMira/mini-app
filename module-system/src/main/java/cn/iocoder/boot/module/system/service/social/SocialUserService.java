package cn.iocoder.boot.module.system.service.social;

import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserRespDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SocialUserService {

    String bindSocialUser(@Valid SocialUserBindReqDTO reqDTO);

    /**
     *  基于社交码获取用户
     *
     *  在认证信息不正确的情况下，也会抛出 {@link ServiceException} 业务异常
     *
     * @param userType
     * @param code
     * @param socialType
     * @param state
     * @return
     */
    SocialUserRespDTO getSocialUserByCode(Integer userType, String code, Integer socialType, String state);

    /**
     *
     * @param userType  用户枚举类型
     * @param loginUserId 用户线程Id
     * @return  用户基本信息
     */
    SocialUserRespDTO getSocialUserByUserId(Integer userType, Long loginUserId, Integer socialType);
}
