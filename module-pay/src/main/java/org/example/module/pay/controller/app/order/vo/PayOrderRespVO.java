package org.example.module.pay.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 支付订单 Response VO")
@Data
@ToString(callSuper = true)
public class PayOrderRespVO {

    @Schema(description = "支付订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
