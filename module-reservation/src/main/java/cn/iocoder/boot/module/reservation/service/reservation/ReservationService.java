package cn.iocoder.boot.module.reservation.service.reservation;

import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppReservationSubmitReqVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppTimeSlotRespVO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import jakarta.validation.Valid;

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
}
