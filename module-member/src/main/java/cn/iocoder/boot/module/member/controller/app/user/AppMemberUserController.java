package cn.iocoder.boot.module.member.controller.app.user;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdatePasswordReqVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdateReqVO;
import cn.iocoder.boot.module.member.convert.MemberUserConvert;
import cn.iocoder.boot.module.member.dal.dataObject.app.user.MemberUserDO;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import cn.iocoder.boot.module.member.service.level.MemberLevelService;
import cn.iocoder.boot.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 用户个人中心")
@RestController
@RequestMapping("/member/user")

public class AppMemberUserController {

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private MemberLevelService memberLevelService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    public CommonResult<AppMemberUserInfoRespVO> getUserInfo(){
        MemberUserDO user = memberUserService.getUser(getLoginUserId());
        MemberLevelDO level = memberLevelService.getLevel(user.getLevelId());
        return success(MemberUserConvert.INSTANCE.convert(user,level));
    }

    @PutMapping("/update")
    @Operation(summary = "修改基本信息")
    public CommonResult<Boolean> updateUser(@RequestBody @Valid AppMemberUserUpdateReqVO reqVO){
        memberUserService.updateUser(getLoginUserId(),reqVO);
        return success(true);
    }

    @PutMapping("update-password")
    @Operation(summary = "修改用户密码",description = "用户修改密码时使用")
    public  CommonResult<Boolean> updatePassword(@RequestBody @Valid AppMemberUserUpdatePasswordReqVO reqVO){
        memberUserService.updateUserPassword(getLoginUserId(),reqVO);
        return success(true);
    }

}
