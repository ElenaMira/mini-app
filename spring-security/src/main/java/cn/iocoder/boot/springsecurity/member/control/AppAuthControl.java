package cn.iocoder.boot.springsecurity.member.control;

import cn.iocoder.boot.springsecurity.common.pojo.CommonResult;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthSmsLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppSendSmsCodeReqVO;
import cn.iocoder.boot.springsecurity.member.service.authService.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtil.getLoginUserId;

/**
 * @author xiaosheng
 */
//@Tag(name = "用户 APP - 认证")
@RestController
@RequestMapping("/member/auth")
@Validated
public class AppAuthControl {
    @Resource
    private AuthService authService;
    @PostMapping("/login")
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> login(@RequestBody @Valid AppAuthLoginReqVO reqVO){
        return CommonResult.success(authService.login(reqVO));
    }

    @PostMapping("/sms-login")
    @Operation(summary = "手机号+验证码登录/注册(自动注册)")
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> smsLogin(@RequestBody @Valid AppAuthSmsLoginReqVO reqVO){
        return CommonResult.success(authService.smsLogin(reqVO));
    }
    @PostMapping("/send-sms-code")
    @Operation(summary = "第三方平台发送验证码")
    @PermitAll
    public CommonResult<Boolean> sendSmsCode(@RequestBody @Valid AppSendSmsCodeReqVO reqVO){
        authService.sendSmsCode(getLoginUserId(),reqVO);
        return CommonResult.success(true);
    }
}
