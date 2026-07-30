package cn.iocoder.boot.module.reservation.dal.mysql.reservation;

import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface GymReservationTimeSlotMapper extends BaseMapperX<GymReservationTimeSlotDO>
{

    default List<GymReservationTimeSlotDO> selectListByStatus(Integer status){
        return selectList(new LambdaQueryWrapperX<GymReservationTimeSlotDO>()
                .eq(GymReservationTimeSlotDO::getStatus,status)
                .eq(GymReservationTimeSlotDO::getDeleted, 0)
                .orderByAsc(GymReservationTimeSlotDO::getSort));
    }
}
