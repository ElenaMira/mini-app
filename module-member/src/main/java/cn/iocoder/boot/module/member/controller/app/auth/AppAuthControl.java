package cn.iocoder.boot.module.member.controller.app.auth;


import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.controller.app.auth.vo.*;
import cn.iocoder.boot.module.member.service.authService.AuthService;

import cn.iocoder.boot.springsecurity.config.SecurityProperties;
import cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;


/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 认证")
@RestController
@RequestMapping("/member/auth")
@Validated
public class AppAuthControl {
    @Resource
    private AuthService authService;

    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/login")
    @Operation(summary = "使用手机 + 密码登录")
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> login(@RequestBody @Valid AppAuthLoginReqVO reqVO){
        return success(authService.login(reqVO));
    }
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken){
        return success(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出系统")
    @PermitAll
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityUtils.obtainToken(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token);
        }
        return success(true);
    }


    @PostMapping("/sms-login")
    @Operation(summary = "手机号+验证码登录/注册(自动注册)")
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> smsLogin(@RequestBody @Valid AppAuthSmsLoginReqVO reqVO){
        return success(authService.smsLogin(reqVO));
    }
    @PostMapping("/send-sms-code")
    @Operation(summary = "第三方平台发送验证码")
    @PermitAll
    public CommonResult<Boolean> sendSmsCode(@RequestBody @Valid AppSendSmsCodeReqVO reqVO){
        authService.sendSmsCode(getLoginUserId(),reqVO);
        return success(true);
    }
    @PostMapping("/social-login")
    @Operation(summary = "社交快捷登录，使用 code 授权码", description = "适合未登录的用户，但是社交账号已绑定用户")
    @PermitAll
    public CommonResult<AppAuthLoginRespVO> socialLogin(@RequestBody @Valid AppAuthSocialLoginReqVO reqVO) {
        return success(authService.socialLogin(reqVO));
    }
}
