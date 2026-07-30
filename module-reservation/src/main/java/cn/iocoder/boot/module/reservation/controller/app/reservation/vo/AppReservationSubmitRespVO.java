package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 提交预约返回结果")
@Data
public class AppReservationSubmitRespVO {
    @Schema(description = "预约订单主键ID")
    private Long id;
}
