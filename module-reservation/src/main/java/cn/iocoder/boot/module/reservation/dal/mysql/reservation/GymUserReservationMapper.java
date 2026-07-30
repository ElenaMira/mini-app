package cn.iocoder.boot.module.reservation.dal.mysql.reservation;

import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface GymUserReservationMapper extends BaseMapperX<GymUserReservationDO> {
}
