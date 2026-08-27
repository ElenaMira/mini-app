package cn.iocoder.boot.module.reservation.job;

import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.module.reservation.dal.mysql.reservation.GymUserReservationMapper;
import cn.iocoder.boot.module.reservation.dal.mysql.reservation.ReservationMapper;
import cn.iocoder.boot.module.reservation.enums.reservation.GymReserveStatusEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.boot.common.util.date.LocalDateTimeUtils.getNow;

/**
 * @author xiaosheng
 */

@Slf4j
@Component
public class GymReservationExpireXxlJob {

    @Resource
    private GymUserReservationMapper userReservationMapper;
    @Resource
    private ReservationMapper reservationMapper;


    /**
     * XX‑Job 任务处理器
     * JobHandler名称：gymReservationExpireJob
     */
    @XxlJob(value = "gymReservationExpireJob")
    @Transactional(rollbackFor = Exception.class)
    public void handle() throws Exception {
        LocalDateTime now = getNow();
        log.info("【健身房过期预约任务】开始执行 now={}", now);

        // 查询过期预约并加行锁。
        List<GymUserReservationDO> expiredList =
                userReservationMapper.selectExpiredPendingReserveForUpdate(
                        now,
                        GymReserveStatusEnum.PENDING.getCode()
                );

        if (expiredList.isEmpty()) {
            log.info("【健身房过期预约任务】无过期预约数据");
            return;
        }
        // 预约 ID
        List<Long> idList = expiredList.stream()
                .map(GymUserReservationDO::getId)
                .toList();
        /*
         * 待使用 -> 爽约
         *
         * SQL 同时限制 reserve_status = PENDING，
         * 即使出现并发情况，也不会把已经处理过的数据再次处理。
         */
        int updateCount = userReservationMapper.batchUpdateMissedByIds(
                idList,
                GymReserveStatusEnum.PENDING.getCode(),
                GymReserveStatusEnum.MISS_APPOINT.getCode()
        );

        if (updateCount != expiredList.size()) {

            throw new IllegalStateException(
                    "过期预约状态更新数量异常，查询数量="
                            + expiredList.size()
                            + "，实际更新数量="
                            + updateCount
            );
        }

        if (updateCount == 0) {
            log.info("【健身房过期预约任务】预约数据已被其他线程处理，本次无需处理");
            return;
        }

        // 按预约日期统计实际需要归还的名额
        Map<LocalDate, Long> restoreCountMap = expiredList.stream()
                .collect(Collectors.groupingBy(
                        GymUserReservationDO::getReserveDate,
                        Collectors.counting()
                ));
        /*
         * 批量归还每日名额。
         */
        restoreCountMap.forEach((reserveDate, count) -> {
            int affectedRows = reservationMapper.increaseRemainPerson(
                    reserveDate,
                    count.intValue()
            );

            if (affectedRows != 1) {
                throw new IllegalStateException(
                        "归还健身房名额失败，reserveDate="
                                + reserveDate
                                + "，count="
                                + count
                                + "，affectedRows="
                                + affectedRows
                );
            }
        });

        log.info("【健身房过期预约任务】执行完成，处理数量={},更新订单={}", expiredList.size(), updateCount);
    }

    /**
     * XX‑Job 任务处理器
     * JobHandler名称：gymReservationExpireJob02
     */
    @XxlJob(value = "gymReservationExpireJob02")
    @Transactional(rollbackFor = Exception.class)
    public void ExpireJob02() throws Exception {

    }
}
