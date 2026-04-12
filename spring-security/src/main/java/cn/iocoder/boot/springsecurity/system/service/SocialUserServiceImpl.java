package cn.iocoder.boot.springsecurity.system.service;

import cn.hutool.core.lang.Assert;
import cn.iocoder.boot.springsecurity.member.dal.dataObject.SocialUserDO;
import cn.iocoder.boot.springsecurity.member.dal.mysql.social.SocialUserMapper;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.springsecurity.system.dal.DO.SocialUserBindDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.social.SocialUserBindMapper;
import cn.iocoder.boot.springsecurity.system.service.social.SocialClientService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.transaction.annotation.Transactional;

import static cn.iocoder.boot.springsecurity.system.uitl.json.JsonUtils.toJsonString;

/**
 * @author xiaosheng
 */
public class SocialUserServiceImpl implements SocialUserService{
    @Resource
    private SocialUserMapper socialUserMapper;
    @Resource
    private SocialUserBindMapper socialUserBindMapper;
    @Resource
    private SocialClientService socialClientService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        //1. 获得社交用户
        SocialUserDO socialUser = authSocialUser(reqDTO.getSocialType(),reqDTO.getUserType(),reqDTO.getCode(),reqDTO.getState());
        Assert.notNull(socialUser, "社交用户不能为空");

        // 2. 社交用户可能之前绑定过别的用户，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndSocialUserId(reqDTO.getUserType(), socialUser.getId());
        // 2. 用户可能之前已经绑定过该社交类型，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(reqDTO.getUserType(),reqDTO.getUserId(), reqDTO.getSocialType());

        //3.绑定当前登录的社交用户
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(reqDTO.getUserId()).userType(reqDTO.getUserType())
                .socialUserId(socialUser.getId()).socialType(socialUser.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
        return socialUser.getOpenid();
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
        // 优先从 DB 中获取第三方授权信息，因为 code 有且可以使用一次。
        // 在社交登录时，当未绑定 User 时，需要绑定登录，此时需要 code 使用两次
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAndState(socialType,code,state);
        if (socialUser!=null){
            return socialUser;
        }

        //获取第三方授权
        AuthUser authUser = socialClientService.getAuthUser(socialType,userType,code,state);
        Assert.notNull(authUser, "三方用户不能为空");

        //保存到DB(覆盖式保存)
        SocialUserDO userDO = socialUserMapper.selectByTypeAndOpenId(socialType, authUser.getUuid());
        if(userDO ==null){
            userDO = new SocialUserDO();
        }
        userDO.setType(socialType).setCode(code).setState(state)
                .setOpenid(authUser.getUuid()).setToken(authUser.getToken().getAccessToken())
                .setRawTokenInfo(toJsonString(authUser.getToken()))
                .setNickname(authUser.getNickname())
                .setAvatar(authUser.getAvatar())
                .setRawUserInfo(toJsonString(authUser.getRawUserInfo()));
        if (userDO.getId()==null){
            socialUserMapper.insert(userDO);
        }else {
            userDO.clean();//清空updateTime,保证使用mybatis时能更新
            socialUserMapper.updateById(userDO);
        }
        return userDO;
    }
}
