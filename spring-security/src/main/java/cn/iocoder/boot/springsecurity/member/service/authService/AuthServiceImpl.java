package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.common.Object.BeanUtils;
import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.common.enums.UserTypeEnum;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppSendSmsCodeReqVO;
import cn.iocoder.boot.springsecurity.member.convert.AuthConvert;
import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.OAuth2TokenCommonApi;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCreateRespDTO;
import cn.iocoder.boot.springsecurity.system.api.sms.SmsCodeApi;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.springsecurity.system.service.social.SocialUserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.common.uitl.servlet.ServletUtils.getClientIP;
import static cn.iocoder.boot.springsecurity.member.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static cn.iocoder.boot.springsecurity.member.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;


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
    public AppAuthLoginRespVO smsLogin(AppAuthLoginReqVO appAuthLoginReqVO) {
        //获取请求体验证码
        String userIp = getClientIP();
        return null;
    }

    @Override
    public void sendSmsCode(Long loginUserId, AppSendSmsCodeReqVO reqVO) {
        //        // 情况 1：如果是修改手机场景，需要校验新手机号是否已经注册，说明不能使用该手机了
        //        if (Objects.equals(reqVO.getScene(), SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene())) {
        //            MemberUserDO user = userService.getUserByMobile(reqVO.getMobile());
        //            if (user != null && !Objects.equals(user.getId(), userId)) {
        //                throw exception(AUTH_MOBILE_USED);
        //            }
        //        }
        //        // 情况 2：如果是重置密码场景，需要校验手机号是存在的
        //        if (Objects.equals(reqVO.getScene(), SmsSceneEnum.MEMBER_RESET_PASSWORD.getScene())) {
        //            MemberUserDO user = userService.getUserByMobile(reqVO.getMobile());
        //            if (user == null) {
        //                throw exception(USER_MOBILE_NOT_EXISTS);
        //            }
        //        }
        //        // 情况 3：如果是修改密码场景，需要查询手机号，无需前端传递
        //        if (Objects.equals(reqVO.getScene(), SmsSceneEnum.MEMBER_UPDATE_PASSWORD.getScene())) {
        //            MemberUserDO user = userService.getUser(userId);
        //            // TODO 芋艿：后续 member user 手机非强绑定，这块需要做下调整；
        //            reqVO.setMobile(user.getMobile());
        //        }
        smsCodeApi.sendSmsCode(AuthConvert.INSTANCE.convert(reqVO).setCreateIp(getClientIP()));
    }

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(MemberUserDO userDO, @NotEmpty(message = "手机号不能为空") @Mobile String mobile, String openid) {
        //1. 创建登录日志
        //2. 创建 Token 令牌
        OAuth2AccessTokenCreateRespDTO respDTO = oauth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(userDO.getId()).setUserType(getUserType().getValue()).setClientId("default")
        );
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
