package cn.iocoder.boot.module.reservation.dal.dataObject.reservation;

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author xiaosheng
 */
@TableName("gym_reservation_time_slot")
@Data
public class GymReservationTimeSlotDO extends BaseDO {

    /**
     * 时段主键ID
     */
    @TableId
    @Schema(description = "时段主键")
    private Long id;

    /**
     * 开始时间 09:00:00
     */
    @Schema(description = "时段开始时间", example = "09:00")
    private LocalTime startTime;

    /**
     * 结束时间 10:30:00
     */
    @Schema(description = "时段结束时间", example = "10:30")
    private LocalTime endTime;

    /**
     * 该时段最大容纳人数
     */
    @Schema(description = "时段最大预约人数", example = "15")
    private Integer maxPerson;

    /**
     * 是否支持预约显示
     */
    @Schema(description = "是否可预约", example = "0")
    private Integer available;

    /**
     * 排序权重（数字越大越靠前）
     */
    @Schema(description = "排序权重")
    private Integer sort;

    /**
     * 状态：0正常 停用
     */
    @Schema(description = "状态：0=正常，1=停用", example = "1")
    private Integer status;
}
