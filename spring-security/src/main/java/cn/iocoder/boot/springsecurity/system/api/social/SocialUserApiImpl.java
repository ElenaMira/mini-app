package cn.iocoder.boot.springsecurity.system.api.social;

import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.springsecurity.system.service.SocialUserService;
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
}
