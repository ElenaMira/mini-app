package cn.iocoder.boot.module.reservation.convert.reservation;

import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppEnableReservationRespVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppTimeSlotRespVO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface ReservationConvert {
    ReservationConvert INSTANCE = Mappers.getMapper(ReservationConvert.class);

    AppEnableReservationRespVO convert(GymReservationDO req);

    List<AppEnableReservationRespVO> convertList(List<GymReservationDO> result);

    @Mapping(source = "maxPerson", target = "remain")
    AppTimeSlotRespVO convert01(GymReservationTimeSlotDO req);

    List<AppTimeSlotRespVO> convertList01(List<GymReservationTimeSlotDO> timeSlot);
}
