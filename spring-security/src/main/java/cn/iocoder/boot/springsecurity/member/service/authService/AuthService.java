package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.member.control.app.auth.vo.*;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface AuthService {
    public AppAuthLoginRespVO login(@Valid AppAuthLoginReqVO appAuthLoginReqVo);

    /**
     * 实现手机号注册和登录
     * @param appAuthLoginReqVO
     * @return
     */
    public AppAuthLoginRespVO smsLogin(@Valid AppAuthSmsLoginReqVO appAuthLoginReqVO);

    public void sendSmsCode(Long loginUserId, @Valid AppSendSmsCodeReqVO appAuthLoginReqVO);

    AppAuthLoginRespVO socialLogin(@Valid AppAuthSocialLoginReqVO reqVO);

    void logout(String token);

    AppAuthLoginRespVO refreshToken(String refreshToken);
}
