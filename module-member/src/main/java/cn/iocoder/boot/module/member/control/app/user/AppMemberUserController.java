package cn.iocoder.boot.module.member.control.app.user;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.control.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.boot.module.member.convert.MemberUserConvert;
import cn.iocoder.boot.module.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.module.member.dal.dataObject.level.MemberLevelDO;
import cn.iocoder.boot.module.member.service.level.MemberLevelService;
import cn.iocoder.boot.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        MemberLevelDO level = memberLevelService.getLevel(user.getId());
        return CommonResult.success(MemberUserConvert.INSTANCE.convert(user,level));
    }
}
