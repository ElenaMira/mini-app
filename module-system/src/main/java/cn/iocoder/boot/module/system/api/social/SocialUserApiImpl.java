package cn.iocoder.boot.module.system.api.social;

import cn.iocoder.boot.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.boot.module.system.service.social.SocialUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class SocialUserApiImpl implements SocialUserApi {
    @Resource
    private SocialUserService socialUserService;

    @Override
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        return  socialUserService.bindSocialUser(reqDTO);
    }

    @Override
    public SocialUserRespDTO getSocialUserByCode(Integer userType, String code, Integer socialType, String state) {
        return socialUserService.getSocialUserByCode(userType,code,socialType,state);
    }

    @Override
    public SocialUserRespDTO getSocialUserByUserId(Integer userType, Long loginUserId, Integer socialType) {
        return socialUserService.getSocialUserByUserId(userType,loginUserId,socialType);
    }
}
