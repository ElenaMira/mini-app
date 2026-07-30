package cn.iocoder.boot.module.reservation.dal.dataObject.reservation;

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author xiaosheng
 */
@TableName(value = "gym_reservation_date")
@Data
public class GymReservationDO extends BaseDO {

    /**
     * 主键id
     */
    @TableId
    @Schema(description = "主键编号")
    private Long id;

    /**
     * 预约日期 yyyy-MM-dd
     */
    @Schema(description = "预约日期", example = "2026-07-25")
    private LocalDate targetDate;

    /**
     * 当日最大可预约人数
     */
    @Schema(description = "当日最大容纳人数", example = "30")
    private Integer maxPerson;

    /**
     * 当日剩余可预约名额
     */
    @Schema(description = "当日剩余预约名额", example = "12")
    private Integer remainPerson;

    /**
     * 是否可预约：0开启 1关闭
     */
    @Schema(description = "是否可预约：0=开启，1=关闭", example = "1")
    private Integer available;

    /**
     * 备注（节假日停业、特殊安排说明）
     */
    @Schema(description = "备注信息")
    private String remark;
}
