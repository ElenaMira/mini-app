package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppSendSmsCodeReqVO;
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
    public AppAuthLoginRespVO smsLogin(@Valid AppAuthLoginReqVO appAuthLoginReqVO);

    public void sendSmsCode(Long loginUserId, @Valid AppSendSmsCodeReqVO appAuthLoginReqVO);
}
