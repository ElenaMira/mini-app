package cn.iocoder.boot.module.member.controller.app.address.vo;

import cn.iocoder.boot.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * todo: 新增名称等常见字段校验
 *
 * @author xiaosheng
 */
@Data
public class AppAddressBaseVO {

    @Schema(description = "收件人名称", requiredMode = Schema.RequiredMode.REQUIRED,example = "zss")
    @NotNull(message = "收件人名称不能为空")
    private String name;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "地区编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "地区编号不能为空")
    private Long areaId;

    @Schema(description = "收件详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收件详细地址不能为空")
    private String detailAddress;

    @Schema(description = "是否默认地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否默认地址不能为空")
    private Boolean defaultStatus;

}
