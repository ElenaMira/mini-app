package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 提交预约请求")
@Data
public class AppReservationSubmitReqVO {
    @NotBlank(message = "预约日期不能为空")
    @Schema(description = "预约日期 yyyy-MM-dd", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-07-28")
    private String date;

    @NotNull(message = "请选择预约时段")
    @Schema(description = "时段id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long timeSlotId;

    @NotBlank(message = "开始时间不能为空")
    @Schema(description = "时段开始时间 HH:mm", example = "09:00")
    private String startTime;

    @NotBlank(message = "结束时间不能为空")
    @Schema(description = "时段结束时间 HH:mm", example = "10:30")
    private String endTime;
}
