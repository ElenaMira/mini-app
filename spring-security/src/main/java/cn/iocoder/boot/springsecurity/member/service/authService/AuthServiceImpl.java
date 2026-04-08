package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;
import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import jakarta.annotation.Resource;

import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.member.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static cn.iocoder.boot.springsecurity.member.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;


/**
 * @author xiaosheng
 */
public class AuthServiceImpl implements AuthService{
    @Resource
    private MemberUserService memberUserService;
    @Override
    public AppAuthLoginRespVO login(AppAuthLoginReqVO reqVO) {
        //1. 判断是否存在合法的登录信息
        MemberUserDO userDO = login0(reqVO.getMobile(), reqVO.getPassword());
        //2.
        String openid = null;
        if(reqVO.getSocialType()!=null){
            // 说明有绑定社交用户(第三方)
            openid =

        }
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
}
