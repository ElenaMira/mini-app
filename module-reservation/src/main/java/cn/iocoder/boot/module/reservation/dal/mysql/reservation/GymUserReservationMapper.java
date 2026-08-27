package cn.iocoder.boot.module.reservation.dal.mysql.reservation;


import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppUserReservationPageReqVO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.module.reservation.enums.reservation.GymReserveStatusEnum;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface GymUserReservationMapper extends BaseMapperX<GymUserReservationDO> {

    default Long selectCountByUserIdAndStatus(Long userId, Integer status) {
        LambdaQueryWrapperX<GymUserReservationDO> wrapper = new LambdaQueryWrapperX<GymUserReservationDO>()
                .eq(GymUserReservationDO::getUserId, userId);
        if (status != null) {
            if (status.equals(GymReserveStatusEnum.CANCEL.getCode())) {
                wrapper.in(
                        GymUserReservationDO::getReserveStatus,
                        GymReserveStatusEnum.MISS_APPOINT.getCode(),
                        GymReserveStatusEnum.CANCEL.getCode()
                );
            } else {
                wrapper.eq(
                        GymUserReservationDO::getReserveStatus,
                        status
                );
            }
        }
        return selectCount(wrapper);



    }
    default PageResult<GymUserReservationDO> selectPage(AppUserReservationPageReqVO reqVO,Long userId) {
        LambdaQueryWrapper<GymUserReservationDO> wrapper = new LambdaQueryWrapperX<GymUserReservationDO>()
                .eq(GymUserReservationDO::getUserId, userId);
        Integer status = reqVO.getStatus();
        if (status != null) {
            if (status.equals(GymReserveStatusEnum.CANCEL.getCode())) {
                // 前端查询“取消”时，同时查询：
                // 4 = 爽约失效
                // 5 = 用户取消
                wrapper.in(
                        GymUserReservationDO::getReserveStatus,
                        GymReserveStatusEnum.MISS_APPOINT.getCode(),
                        GymReserveStatusEnum.CANCEL.getCode()
                );
            } else {
                wrapper.eq(
                        GymUserReservationDO::getReserveStatus,
                        status
                );
            }
        }
        wrapper.orderByDesc(GymUserReservationDO::getId);
        return selectPage(reqVO,wrapper);
    }

    /**
     * 查询过期、待使用预约
     */
    List<GymUserReservationDO> selectExpiredPendingReserveForUpdate(
            @Param("now") LocalDateTime now,
            @Param("pendingStatus") Integer pendingStatus
    );

    /**
     * 批量更新爽约状态
     */
    int batchUpdateMissedByIds(@Param("idList") List<Long> idList,@Param("pendingStatus") Integer pendingStatus,@Param("missedStatus") Integer missedStatus);

}
