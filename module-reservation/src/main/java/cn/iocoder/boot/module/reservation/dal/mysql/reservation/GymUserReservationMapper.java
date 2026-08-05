package cn.iocoder.boot.module.reservation.dal.mysql.reservation;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppUserReservationPageReqVO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

/**
 * @author xiaosheng
 */
@Mapper
public interface GymUserReservationMapper extends BaseMapperX<GymUserReservationDO> {

    default Long selectCountByUserIdAndStatus(Long userId, Integer status) {
        return selectCount(new LambdaQueryWrapperX<GymUserReservationDO>()
                .eq(GymUserReservationDO::getUserId, userId)
                .eq(GymUserReservationDO::getReserveStatus, status));
    }
    default PageResult<GymUserReservationDO> selectPage(AppUserReservationPageReqVO reqVO,Long userId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<GymUserReservationDO>()
                .eq(GymUserReservationDO::getUserId,userId)
                .eqIfPresent(GymUserReservationDO::getReserveStatus,reqVO.getStatus())
                .orderByDesc(GymUserReservationDO::getId));
    }
}
