package cn.iocoder.boot.module.reservation.dal.mysql.reservation;

import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    /**
     * 批量归还剩余名额。
     */
    default int increaseRemainPerson(LocalDate reserveDate,int addNum) {
        LambdaUpdateWrapper<GymReservationDO> wrapper = Wrappers.lambdaUpdate();
        // 数据库层面自增，原子操作
        wrapper.setSql("remain_person = remain_person + " + addNum)
                .eq(GymReservationDO::getTargetDate, reserveDate);
        return update(null, wrapper);
    }
}
