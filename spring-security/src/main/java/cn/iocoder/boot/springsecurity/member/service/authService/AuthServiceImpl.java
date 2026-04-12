package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.common.Object.BeanUtils;
import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.common.enums.UserTypeEnum;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;
import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.OAuth2TokenCommonApi;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCreateRespDTO;
import cn.iocoder.boot.springsecurity.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.boot.springsecurity.system.service.SocialUserService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;

import static cn.iocoder.boot.springsecurity.common.Object.BeanUtils.toBean;
import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.member.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static cn.iocoder.boot.springsecurity.member.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;


/**
 * @author xiaosheng
 */
public class AuthServiceImpl implements AuthService{
    @Resource
    private MemberUserService memberUserService;
    @Resource
    private SocialUserService socialUserService;

    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;

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
