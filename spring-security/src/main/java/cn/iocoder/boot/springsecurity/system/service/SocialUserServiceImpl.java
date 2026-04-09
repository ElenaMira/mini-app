package cn.iocoder.boot.springsecurity.system.service;

import cn.iocoder.boot.springsecurity.member.dal.dataObject.SocialUserDO;
import cn.iocoder.boot.springsecurity.member.dal.mysql.social.SocialUserMapper;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public class SocialUserServiceImpl implements SocialUserService{
    @Resource
    private SocialUserMapper socialUserMapper;
    @Override
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        //1. 查询是否已经存在绑定用户
        authSocialUser
    }
    /**
     * 授权获得对应的社交用户
     * 如果授权失败，则会抛出 {@link ServiceException} 异常
     *
     * @param socialType 社交平台的类型 {@link SocialTypeEnum}
     * @param userType 用户类型
     * @param code     授权码
     * @param state    state
     * @return 授权用户
     */
    @NotNull
    public SocialUserDO authSocialUser(Integer socialType, Integer userType, String code, String state){
        // 优先从 DB 中获取，因为 code 有且可以使用一次。
        // 在社交登录时，当未绑定 User 时，需要绑定登录，此时需要 code 使用两次
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAndState(socialType,code,state);
        if (socialUser!=null){
            return socialUser;
        }

        //获取第三方授权
        socialClientService.

    }
}
