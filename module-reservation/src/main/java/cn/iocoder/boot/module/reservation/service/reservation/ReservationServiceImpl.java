package cn.iocoder.boot.module.reservation.service.reservation;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppReservationSubmitReqVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppTimeSlotRespVO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.module.reservation.dal.mysql.reservation.GymReservationTimeSlotMapper;
import cn.iocoder.boot.module.reservation.dal.mysql.reservation.GymUserReservationMapper;
import cn.iocoder.boot.module.reservation.dal.mysql.reservation.ReservationMapper;
import cn.iocoder.boot.module.reservation.enums.ErrorCodeConstant;
import cn.iocoder.boot.module.reservation.enums.reservation.GymReserveStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.boot.common.enums.CommonStatusEnum.DISABLE;
import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.module.reservation.enums.ErrorCodeConstant.RESERVATION_DATE_EXPIRED;

/**
 * @author xiaosheng
 */
@Service
public class ReservationServiceImpl implements ReservationService {
    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private GymReservationTimeSlotMapper timeSlotMapper;

    @Resource
    private GymUserReservationMapper userReservationMapper;

    @Override
    public List<GymReservationDO> getEnableReservations(String month) {

        // 1. 解析年月，计算起止时间
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.plusMonths(1).atDay(1);
        LocalDate today = LocalDate.now();

        // 起始时间不能早于今天
        LocalDate start = monthStart.isAfter(today) ? monthStart : today;

        // 2. 查询当月所有可预约日期配置
        return reservationMapper.selectListByMonth(start, monthEnd);
    }

    @Override
    public List<GymReservationTimeSlotDO> getTimeSlot(String date) {
        LocalDate targetDate = LocalDate.parse(date);
        // 校验：日期不能是过去时间
        validationDate(targetDate);

        //校验date是否开启预约
        GymReservationDO gymReservationDO = reservationMapper.selectByTargetDate(targetDate);
        // 当日无配置 / 关闭预约，返回空
        if (gymReservationDO == null || Objects.equals(gymReservationDO.getAvailable(), DISABLE.getStatus())) {
            return List.of();
        }


        return timeSlotMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitReservation(AppReservationSubmitReqVO reqVO,Long  userId) {
        LocalDate reserveDate = LocalDate.parse(reqVO.getDate());
        // 公共日期校验
        validationDate(reserveDate);

        GymReservationDO gymReservationDO = reservationMapper.selectByTargetDate(reserveDate);
        if (gymReservationDO==null || Objects.equals(gymReservationDO.getAvailable(),DISABLE.getStatus())) {
            throw exception(ErrorCodeConstant.RESERVATION_DATE_CLOSED);
        }

        GymReservationTimeSlotDO slotDO = timeSlotMapper.selectById(reqVO.getTimeSlotId());
        if (slotDO == null || Objects.equals(slotDO.getStatus(),DISABLE.getStatus()) || Objects.equals(slotDO.getDeleted(),DISABLE.getStatus())) {
            throw exception(ErrorCodeConstant.RESERVATION_SLOT_INVALID);
        }
        userReservationMapper.insert(GymUserReservationDO.builder()
                        .reserveStatus(GymReserveStatusEnum.PENDING.getCode())
                        .userId(userId)
                        .reserveDate(reserveDate)
                        .cancelTime(null)
                        .timeSlotId(reqVO.getTimeSlotId())
                        .startTime(LocalTime.parse(reqVO.getStartTime()))
                        .endTime(LocalTime.parse(reqVO.getEndTime()))
                .build());

        slotDO.setMaxPerson(slotDO.getMaxPerson() - 1);
        gymReservationDO.setMaxPerson(gymReservationDO.getMaxPerson()-1);
        if (slotDO.getMaxPerson() <= 0) {
            slotDO.setAvailable(CommonStatusEnum.DISABLE.getStatus());
        }
        if (gymReservationDO.getMaxPerson() <= 0) {
            gymReservationDO.setAvailable(CommonStatusEnum.DISABLE.getStatus());
        }
        reservationMapper.updateById(gymReservationDO);
        timeSlotMapper.updateById(slotDO);
    }

    private void validationDate(LocalDate date){
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw exception(ErrorCodeConstant.RESERVATION_DATE_EXPIRED);
        }
    }
}

