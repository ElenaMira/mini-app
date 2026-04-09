package cn.iocoder.boot.springsecurity.member.control.vo;

import cn.iocoder.boot.springsecurity.common.enums.SocialTypeEnum;
import cn.iocoder.boot.springsecurity.common.validation.InEnum;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author xiaosheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "用户 APP - 手机 + 密码登录 Request VO,如果登录并绑定社交用户，需要传递 social 开头的参数")
public class AppAuthLoginReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotEmpty(message = "手机号不能为空")
    @Mobile
    private String mobile;
    @Schema(description = "密码",requiredMode = Schema.RequiredMode.REQUIRED,example = "buzhidao")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4,max = 16,message = "密码长度为 4-16 位")
    private String password;
    @Schema(description = "社交平台的类型，参见 SocialTypeEnum 枚举值", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @InEnum(SocialTypeEnum.class)
    private Integer socialType;

    @Schema(description = "授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String socialCode;

    @Schema(description = "state", requiredMode = Schema.RequiredMode.REQUIRED, example = "9b2ffbc1-7425-4155-9894-9d5c08541d62")
    private String socialState;
}
