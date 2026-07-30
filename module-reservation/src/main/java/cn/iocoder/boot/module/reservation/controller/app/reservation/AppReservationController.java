package cn.iocoder.boot.module.reservation.controller.app.reservation;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppEnableReservationRespVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppReservationSubmitReqVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppReservationSubmitRespVO;
import cn.iocoder.boot.module.reservation.controller.app.reservation.vo.AppTimeSlotRespVO;
import cn.iocoder.boot.module.reservation.convert.reservation.ReservationConvert;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymReservationTimeSlotDO;
import cn.iocoder.boot.module.reservation.dal.dataObject.reservation.GymUserReservationDO;
import cn.iocoder.boot.module.reservation.service.reservation.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
