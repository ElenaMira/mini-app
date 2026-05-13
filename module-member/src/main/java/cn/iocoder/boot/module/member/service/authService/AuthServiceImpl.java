package cn.iocoder.boot.module.member.service.authService;

import cn.hutool.core.lang.Assert;

import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenBaseRespDTO;
import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.common.enums.UserTypeEnum;
import cn.iocoder.boot.common.validation.Mobile;
import cn.iocoder.boot.module.member.control.app.auth.vo.*;
import cn.iocoder.boot.module.member.convert.AuthConvert;
import cn.iocoder.boot.module.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.module.member.service.user.MemberUserService;

import cn.iocoder.boot.module.system.api.sms.SmsCodeApi;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.boot.module.system.enums.sms.SmsSceneEnum;
import cn.iocoder.boot.module.system.service.social.SocialUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.common.uitl.servlet.ServletUtils.getClientIP;
import static cn.iocoder.boot.module.member.enums.ErrorCodeConstants.*;
import static cn.iocoder.boot.web.web.core.util.WebFrameworkUtils.getTerminal;


/**
 * @author xiaosheng
 */
@Service
public class AuthServiceImpl implements AuthService{
    @Resource
    private MemberUserService memberUserService;
    @Resource
    private SocialUserService socialUserService;

    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;

    @Resource
    private SmsCodeApi smsCodeApi;

    @Override
    public AppAuthLoginRespVO login(AppAuthLoginReqVO reqVO) {
        //1. 判断是否存在合法的登录信息
        MemberUserDO userDO = login0(reqVO.getMobile(), reqVO.getPassword());
        //2.
        String openid = null;
        if(reqVO.getSocialType()!=null){
            // 说明有绑定社交用户(第三方)
            openid = socialUserService.bindSocialUser(new SocialUserBindReqDTO(
                    userDO.getId()
                    ,getUserType().getValue()
                    ,reqVO.getSocialType(),reqVO.getSocialCode(), reqVO.getSocialState()
            ));
        }
        // 创建 Token 令牌，记录登录日志
//        return createTokenAfterLoginSuccess(userDO, reqVO.getMobile(), LoginLogTypeEnum.LOGIN_MOBILE, openid);
        return createTokenAfterLoginSuccess(userDO, reqVO.getMobile(),openid);   
    }

    @Override
    @Transactional
    public AppAuthLoginRespVO smsLogin(AppAuthSmsLoginReqVO reqVO) {
        //校验并使用验证码
        String userIp = getClientIP();
        smsCodeApi.useSmsCode(AuthConvert.INSTANCE.convert(reqVO,SmsSceneEnum.MEMBER_LOGIN.getScene(),userIp));

        //获得|注册用户
        MemberUserDO userDO = memberUserService.createUserIfAbsent(reqVO.getMobile(), userIp, getTerminal());
        Assert.notNull(userDO,"获取用户失败,结果为null");

        // 校验是否被禁止
        if(CommonStatusEnum.isDisable(userDO.getStatus())){
            //todo: 日志
//            createLoginLog(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_SMS, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        // 如果 socialType 非空，说明需要绑定社交用户
        String openid = null;
        if (reqVO.getSocialType()!=null){
            openid = socialUserService.bindSocialUser(SocialUserBindReqDTO.builder()
                    .code(reqVO.getCode())
                    .state(reqVO.getSocialState())
                    .userId(userDO.getId())
                    .socialType(reqVO.getSocialType())
                    .userType(getUserType().getValue())
                    .build()
            );
        }
        return createTokenAfterLoginSuccess(userDO,reqVO.getMobile(),openid);
    }

    @Override
    public void sendSmsCode(Long loginUserId, AppSendSmsCodeReqVO reqVO) {
        // 情况 1：如果是修改手机场景，需要校验新手机号是否已经注册，说明不能使用该手机了
        if (Objects.equals(reqVO.getScene(), SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene())) {
            MemberUserDO user = memberUserService.getUserByMobile(reqVO.getMobile());
            if (user != null && !Objects.equals(user.getId(), loginUserId)) {
                throw exception(AUTH_MOBILE_USED);
            }
        }
        // 情况 2：如果是重置密码场景，需要校验手机号是存在的
        if (Objects.equals(reqVO.getScene(), SmsSceneEnum.MEMBER_RESET_PASSWORD.getScene())) {
            MemberUserDO user = memberUserService.getUserByMobile(reqVO.getMobile());
            if (user == null) {
                throw exception(USER_MOBILE_NOT_EXISTS);
            }
        }
        // 情况 3：如果是修改密码场景，需要查询手机号，无需前端传递
        if (Objects.equals(reqVO.getScene(), SmsSceneEnum.MEMBER_UPDATE_PASSWORD.getScene())) {
            MemberUserDO user = memberUserService.getUser(loginUserId);
            // TODO 后续 member user 手机非强绑定，这块需要做下调整；(手机号可能非强制绑定)
            reqVO.setMobile(user.getMobile());
        }
        smsCodeApi.sendSmsCode(AuthConvert.INSTANCE.convert(reqVO).setCreateIp(getClientIP()));
    }

    @Override
    public AppAuthLoginRespVO socialLogin(AppAuthSocialLoginReqVO reqVO) {
        SocialUserRespDTO socialUser = socialUserService.getSocialUserByCode(UserTypeEnum.MEMBER.getValue(), reqVO.getCode(),
                reqVO.getType(), reqVO.getState());
        if (socialUser == null){
            throw exception(AUTH_SOCIAL_USER_NOT_FOUND);
        }
        // 情况一：已绑定，直接读取用户信息
        MemberUserDO user;
        if(socialUser.getUserId()!=null){
            user =  memberUserService.getUser(socialUser.getUserId());
        }else {
            // 情况二：未绑定，注册用户 + 绑定用户
            user =  memberUserService.createUser(socialUser.getNickname(),socialUser.getAvatar()
                    ,getClientIP(),getTerminal());
            socialUserService.bindSocialUser(SocialUserBindReqDTO.builder()
                            .userId(user.getId())
                            .code(reqVO.getCode())
                            .socialType(reqVO.getType())
                            .userType(getUserType().getValue())
                            .state(reqVO.getState())
                    .build());
        }
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return createTokenAfterLoginSuccess(user,user.getMobile(), socialUser.getOpenid());
    }

    @Override
    public void logout(String token) {
        OAuth2AccessTokenBaseRespDTO respDTO = oauth2TokenApi.removeAccessToken(token);
        if (respDTO == null){
            return;
        }
        // 删除成功，则记录登出日志
//        createLogoutLog(accessTokenRespDTO.getUserId());
    }

    @Override
    public AppAuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenBaseRespDTO baseRespDTO = oauth2TokenApi.refreshToken(refreshToken, "default");
        return AuthConvert.INSTANCE.convert(baseRespDTO, null);
    }

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(MemberUserDO userDO, @Mobile String mobile, String openid) {
        //1. todo : 创建登录日志,新增枚举LoginLogTypeEnum
        //2. 创建 Token 令牌
        OAuth2AccessTokenBaseRespDTO respDTO = oauth2TokenApi.createAccessToken(OAuth2AccessTokenCreateReqDTO.builder()
                        .userId(userDO.getId())
                        .userType(getUserType().getValue())
                        .clientId("default")
                        .build());
        return BeanUtils.toBean(respDTO,AppAuthLoginRespVO.class);
    }

    public MemberUserDO login0(String mobile, String password){
        //1. 基于手机号查数据(手机号唯一)
        MemberUserDO memberUser = memberUserService.getMemberUser(mobile);
        // 判断是否为空
        if(memberUser==null){
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        //判断密码是否匹配
        if(!memberUserService.isPasswordMatch(memberUser.getPassword(),password)){
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        //判断用户是否可用
        if (!CommonStatusEnum.isDisable(memberUser.getStatus())){
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        return memberUser;
    }
    private UserTypeEnum getUserType() {
        return UserTypeEnum.MEMBER;
    }
}
