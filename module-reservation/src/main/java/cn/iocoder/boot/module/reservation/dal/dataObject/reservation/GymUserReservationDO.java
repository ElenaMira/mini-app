package cn.iocoder.boot.module.reservation.dal.dataObject.reservation;

/**
 * @author xiaosheng
 */

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 用户健身房预约订单 DO
 */
@TableName(value = "gym_user_reservation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GymUserReservationDO extends BaseDO {

    /**
     * 预约订单主键id
     */
    @TableId
    @Schema(description = "预约订单ID")
    private Long id;

    /**
     * 预约用户ID
     */
    @Schema(description = "预约会员用户id")
    private Long userId;

    /**
     * 预约日期
     */
    @Schema(description = "预约日期", example = "2026-07-25")
    private LocalDate reserveDate;

    /**
     * 预约时段id（关联时段表）
     */
    @Schema(description = "预约时段编号")
    private Long timeSlotId;

    /**
     * 时段开始时间（冗余存储，方便查询）
     */
    @Schema(description = "时段开始时间")
    private LocalTime startTime;

    /**
     * 时段结束时间（冗余存储）
     */
    @Schema(description = "时段结束时间")
    private LocalTime endTime;

    /**
     * 预约状态：
     * 1 待使用 | 2 已核销 | 3 已取消 | 4 爽约失效
     */
    @Schema(description = "预约状态：1待使用 2已核销 3已取消 4爽约")
    private Integer reserveStatus;

    /**
     * 用户取消预约时间
     */
    @Schema(description = "取消预约时间")
    private LocalDateTime cancelTime;

    /**
     * 到店签到核销时间
     */
    @Schema(description = "到店核销时间")
    private LocalDateTime checkInTime;
}