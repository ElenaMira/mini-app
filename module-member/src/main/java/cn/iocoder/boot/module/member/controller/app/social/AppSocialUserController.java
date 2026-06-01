package cn.iocoder.boot.module.member.controller.app.social;

import cn.iocoder.boot.common.enums.UserTypeEnum;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.controller.app.social.vo.AppMemberUserUpdateMobileByWeixinReqVO;
import cn.iocoder.boot.module.member.controller.app.social.vo.AppSocialUserBindReqVO;
import cn.iocoder.boot.module.system.api.social.SocialUserApi;
import cn.iocoder.boot.module.system.api.social.dto.SocialUserBindReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 社交用户")
@RestController
@RequestMapping("/member/social-user")
@Validated
public class AppSocialUserController {
    @Resource
    private SocialUserApi  socialUserApi;


    @PostMapping("/bind")
    @Operation(summary = "社交绑定，使用 code 授权码")
    @PermitAll
    public CommonResult<String> socialBind(@RequestBody @Valid AppSocialUserBindReqVO reqVO) {
        SocialUserBindReqDTO reqDTO = SocialUserBindReqDTO.builder()
                .userId(getLoginUserId())
                .userType(UserTypeEnum.MEMBER.getValue())
                .socialType(reqVO.getType())
                .state(reqVO.getState())
                .code(reqVO.getCode())
                .build();
        String openid = socialUserApi.bindSocialUser(reqDTO);
        return success(openid);
    }
}
