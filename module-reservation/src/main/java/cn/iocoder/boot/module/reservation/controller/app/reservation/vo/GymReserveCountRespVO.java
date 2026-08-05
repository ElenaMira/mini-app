package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 用户预约各状态数量统计VO")
@Data
public class GymReserveCountRespVO {
    @Schema(description = "待预约数量")
    private Integer pendingCount;

    @Schema(description = "使用中数量")
    private Integer usingCount;

    @Schema(description = "已完成数量")
    private Integer finishedCount;

    @Schema(description = "已取消数量")
    private Integer cancelCount;
}
