package cn.iocoder.boot.springsecurity.member.control;

import cn.iocoder.boot.springsecurity.common.pojo.CommonResult;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginReqVO;
import cn.iocoder.boot.springsecurity.member.control.vo.AppAuthLoginRespVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaosheng
 */
//@Tag(name = "用户 APP - 认证")
@RestController
@RequestMapping("/member/auth")
@Validated
public class AppAuthControl {
    @Resource
    private AuthLoginService authService;
    @PostMapping("/login")
    public CommonResult<AppAuthLoginRespVO> login(@RequestBody @Valid AppAuthLoginReqVO){
        return CommonResult.success(authService);
    }
}
