package cn.iocoder.boot.module.member.controller.app.auth.vo;

import cn.iocoder.boot.common.validation.InEnum;
import cn.iocoder.boot.common.validation.Mobile;
import cn.iocoder.boot.module.system.enums.sms.SmsSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author xiaosheng
 */
@Data
public class AppSendSmsCodeReqVO {
    @Schema(description = "手机号",example = "15601691234")
    @Mobile
    private String mobile;

    @Schema(description = "发送场景,对应 SmsSceneEnum 枚举",example = "1")
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;
}
