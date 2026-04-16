package cn.iocoder.boot.springsecurity.member.control.vo;

import cn.iocoder.boot.springsecurity.common.validation.InEnum;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;
import cn.iocoder.boot.springsecurity.system.enums.sms.SmsSceneEnum;
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
