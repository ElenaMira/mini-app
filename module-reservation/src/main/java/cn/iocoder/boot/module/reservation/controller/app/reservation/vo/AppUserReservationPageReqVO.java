package cn.iocoder.boot.module.reservation.controller.app.reservation.vo;

import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.validation.InEnum;
import cn.iocoder.boot.module.reservation.enums.reservation.GymReserveStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Schema(description = "用户App - 用户预约分页reqVO")
@Data
public class AppUserReservationPageReqVO extends PageParam {
    @Schema(description = "status状态类型",example = "1")
    @InEnum(value = GymReserveStatusEnum.class, message = "订单状态必须是 {value}")
    private Integer status;
}
