package cn.iocoder.boot.module.reservation.controller.app.reservation;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.*;
import cn.iocoder.boot.module.reservation.convert.reservation.ReservationConvert;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.module.reservation.enums.reservation.GymReserveStatusEnum;
import cn.iocoder.boot.module.reservation.service.reservation.ReservationService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 预约")
@RestController
@RequestMapping("/gym/reservation")
@Validated
public class AppReservationController {
    @Resource
    private ReservationService reservationService;


    @GetMapping("monthly-availability")
    @Operation(summary = "获取预约时间")
    @Parameter(name = "month",description = "获取预约时间的月份",required = true,example = "2026-07")
    public CommonResult<List<AppEnableReservationRespVO>> getReservationDate(@RequestParam(value = "month")String month){
        List<GymReservationDO> result = reservationService.getEnableReservations(month);
        return success(ReservationConvert.INSTANCE.convertList(result));
    }


    @GetMapping("time-slots")
    @Operation(summary = "获取预约时间")
    @Parameter(name = "date",description = "获取date预约信息",required = true,example = "2026-07-25")
    public CommonResult<List<AppTimeSlotRespVO>> getTimeSlot(@RequestParam(value = "date")String date){
        List<GymReservationTimeSlotDO> timeSlot = reservationService.getTimeSlot(date);
        return success(ReservationConvert.INSTANCE.convertList01(timeSlot));
    }

    @PostMapping("submit")
    @Operation(summary = "提交用户预约单")
    public CommonResult<Integer> submit(@RequestBody AppReservationSubmitReqVO reqVO){
        reservationService.submitReservation(reqVO,getLoginUserId());
        return success(0);
    }

    @GetMapping("/get-count")
    @Operation(summary = "获取预约数量")
    public CommonResult<Map<String,Long>> getCount(){
        LinkedHashMap<String,Long> reservationCount = Maps.newLinkedHashMapWithExpectedSize(4);
        reservationCount.put("pendingCount",reservationService.getUserReserveSingleCount(getLoginUserId(),
                GymReserveStatusEnum.PENDING.getCode()));
        // 使用中
        reservationCount.put("usingCount", reservationService.getUserReserveSingleCount(getLoginUserId(),
                GymReserveStatusEnum.CHECK_IN.getCode()));
        // 已完成
        reservationCount.put("finishedCount", reservationService.getUserReserveSingleCount(getLoginUserId(),
                GymReserveStatusEnum.FINISHED.getCode()));
        // 已取消
        reservationCount.put("cancelCount", reservationService.getUserReserveSingleCount(getLoginUserId(),
                GymReserveStatusEnum.CANCEL.getCode()));
        return success(reservationCount);

    }
    @GetMapping("/page")
    @Operation(summary = "获得预约分页")
    public CommonResult<PageResult<AppUserReservationPageRespVO >> getOrderPage(AppUserReservationPageReqVO reqVO) {
        // 查询用户预约单
        PageResult<GymUserReservationDO> pageResult = reservationService.getPage(getLoginUserId(), reqVO);

        // 最终组合
        List<AppUserReservationPageRespVO> voList = ReservationConvert.INSTANCE.convertList02(pageResult.getList());

        LocalDate today = LocalDate.now();

        // 3. 填充衍生字段 isToday / canCancel
        voList.forEach(vo -> {
            // 是否今日预约
            vo.setIsToday(today.isEqual(vo.getReserveDate()));

            // 是否可以取消：待核销状态 + 预约日期不早于今天
            boolean canCancel = GymReserveStatusEnum.PENDING.getCode().equals(vo.getReserveStatus())
                    && !vo.getReserveDate().isBefore(today);
            vo.setCanCancel(canCancel);
        });

        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

}
