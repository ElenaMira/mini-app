package cn.iocoder.boot.module.member.controller.app.level;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.controller.app.level.vo.AppMemberLevelRespVO;
import cn.iocoder.boot.module.member.convert.level.MemberLevelConvert;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import cn.iocoder.boot.module.member.service.level.MemberLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.boot.common.pojo.CommonResult.success;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 会员等级")
@RestController
@RequestMapping("/member/level")
@Validated
public class AppMemberLevelController {
    @Resource
    private MemberLevelService levelService;

    @GetMapping("/list")
    @Operation(summary = "获得会员等级列表")
    @PermitAll
    public CommonResult<List<AppMemberLevelRespVO>> getLevelList() {
        List<MemberLevelDO> result = levelService.getEnableLevelList();
        return success(MemberLevelConvert.INSTANCE.convertList02(result));
    }

}
