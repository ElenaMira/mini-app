package cn.iocoder.boot.module.reservation.service.reservation;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppReservationSubmitReqVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppTimeSlotRespVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppUserReservationPageReqVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.GymReserveCountRespVO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface ReservationService {
    /**
     * 基于当前月份获取对应可预约时间表
     * @param month 月份
     * @return
     */
    List<GymReservationDO> getEnableReservations(String month);

    /**
     * 获取预约时段信息
     * @param date 日期
     * @return
     */
    List<GymReservationTimeSlotDO> getTimeSlot(String date);

    void submitReservation(@Valid  AppReservationSubmitReqVO reqVO, Long userId);

    /**
     * 基于状态获取用户预约统计数量
     * @param userId 用户ID
     * @param status 预约状态
     * @return 数量统计
     */
    Long getUserReserveSingleCount(Long userId,Integer status);

    /**
     * 获取不同状态的分页预约数据
     * @param loginUserId
     * @param reqVO
     * @return
     */
    PageResult<GymUserReservationDO> getPage(@NotNull Long loginUserId, AppUserReservationPageReqVO reqVO);
}
