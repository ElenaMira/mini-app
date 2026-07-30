package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 单日可预约时段VO")
@Data
public class AppTimeSlotRespVO {
    @Schema(description = "时段id")
    private Long id;

    @Schema(description = "开始时间 HH:mm", example = "09:00")
    private String startTime;

    @Schema(description = "结束时间 HH:mm", example = "10:30")
    private String endTime;

    @Schema(description = "剩余预约人数")
    private Integer remain;

    @Schema(description = "0可预约 1不可预约(已满/停用/过期)")
    private Integer available;
}
