package cn.iocoder.boot.module.promotion.controller.app.diy;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.promotion.controller.app.diy.vo.AppDiyTemplatePropertyRespVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyPageDO;
import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyTemplateDO;
import cn.iocoder.boot.module.promotion.enums.diy.DiyPageEnum;
import cn.iocoder.boot.module.promotion.service.diy.DiyPageService;
import cn.iocoder.boot.module.promotion.service.diy.DiyTemplateService;
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
import static cn.iocoder.boot.common.uitl.collection.CollectionUtils.findFirst;

/**
 * @author xiaosheng
 */

@Tag(name = "用户 APP - 装修模板")
@RestController
@RequestMapping("/promotion/diy-template")
@Validated
public class AppDiyTemplateController {
    @Resource
    private DiyTemplateService diyTemplateService;

    @Resource
    private DiyPageService diyPageService;

    @GetMapping("/used")
    @Operation(summary = "使用中的装修模板")
    @PermitAll
    public CommonResult<AppDiyTemplatePropertyRespVO> getUsedDiyTemplate() {
        DiyTemplateDO diyTemplate = diyTemplateService.getUsedDiyTemplate();
        return success(buildVo(diyTemplate));
    }
    private AppDiyTemplatePropertyRespVO buildVo(DiyTemplateDO diyTemplate) {
        if (diyTemplate == null) {
            return null;
        }
        // 查询模板下的页面
        List<DiyPageDO> pages = diyPageService.getDiyPageByTemplateId(diyTemplate.getId());
        String home = findFirst(pages, page -> DiyPageEnum.INDEX.getName().equals(page.getName()), DiyPageDO::getProperty);
        String user = findFirst(pages, page -> DiyPageEnum.MY.getName().equals(page.getName()), DiyPageDO::getProperty);
        // 拼接返回
        return DiyTemplateConvert.INSTANCE.convertPropertyVo2(diyTemplate, home, user);
    }
}
