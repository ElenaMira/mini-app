package cn.iocoder.boot.module.reservation.dal.mysql.reservation;

import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface ReservationMapper extends BaseMapperX<GymReservationDO> {
    default List<GymReservationDO> selectListByMonth(LocalDate startDate, LocalDate nextMonthFirstDay) {
        return selectList(new LambdaQueryWrapperX<GymReservationDO>()
                .ge(GymReservationDO::getTargetDate, startDate)
                .lt(GymReservationDO::getTargetDate, nextMonthFirstDay)
                .orderByAsc(GymReservationDO::getTargetDate)
        );
    }

    default GymReservationDO selectByTargetDate(LocalDate date){
        return selectOne(GymReservationDO::getTargetDate, date);
    }

}
