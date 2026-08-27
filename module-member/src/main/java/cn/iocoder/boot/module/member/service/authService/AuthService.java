package cn.iocoder.boot.module.member.service.authService;

import cn.iocoder.boot.module.member.controller.app.auth.vo.*;
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

    /**
     * 基于授权码登录
     * @param reqVO
     * @return
     */
    AppAuthLoginRespVO socialLogin(@Valid AppAuthSocialLoginReqVO reqVO);

    void logout(String token);

    AppAuthLoginRespVO refreshToken(String refreshToken);
    /**
     * 微信小程序的一键登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AppAuthLoginRespVO weixinMiniAppLogin(@Valid AppAuthWeixinMiniAppLoginReqVO reqVO);
}
