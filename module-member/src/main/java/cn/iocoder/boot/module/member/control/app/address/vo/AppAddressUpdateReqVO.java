package cn.iocoder.boot.module.member.control.app.address.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Schema(description = "用户 APP - 用户收件地址更新 Request VO")
@Data
public class AppAddressUpdateReqVO extends AppAddressBaseVO{

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;
}
