package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 可预约时间")
@Data
public class AppEnableReservationRespVO {
    @Schema(description = "预约日期 yyyy-MM-dd", example = "2026-07-25")
    private LocalDate targetDate;

    @Schema(description = "是否可预约", example = "0")
    private Integer  available;

    @Schema(description = "当日剩余名额", example = "6")
    private Integer remainPerson;
}
