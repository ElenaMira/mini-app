package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;

/**
 * @author xiaosheng
 */
public interface AuthService {
    public AppAuthLoginRespVO login(AppAuthLoginReqVO appAuthLoginReqVo);
}
