package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 用户预约分页VO")
@Data
public class AppUserReservationPageRespVO {
    @Schema(description = "预约id")
    private Long id;

    @Schema(description = "预约日期 yyyy-MM-dd")
    private LocalDate reserveDate;

    @Schema(description = "开始时间 HH:mm:ss")
    private LocalTime startTime;

    @Schema(description = "结束时间 HH:mm:ss")
    private LocalTime endTime;

    @Schema(description = "预约状态 0待核销 1已完成 2已取消 3爽约作废")
    private Integer reserveStatus;

    // 前端渲染衍生字段，后端直接组装返回
    @Schema(description = "是否今天预约")
    private Boolean isToday;

    @Schema(description = "是否可以取消预约")
    private Boolean canCancel;
}
