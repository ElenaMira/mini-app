package cn.iocoder.boot.module.pay.controller.app.order;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.service.order.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.iocoder.boot.module.pay.controller.app.order.vo.PayOrderRespVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

@Tag(name = "用户 APP - 支付订单")
@RestController
@RequestMapping("/pay/order")
@Validated
@Slf4j
public class AppPayOrderControl {
    @Resource
    private PayOrderService payOrderService;


    @GetMapping("/get")
    @Operation(summary = "获取支付订单")
    @Parameters({
            @Parameter(name = "id", description = "编号", example = "1024"),
            @Parameter(name = "no", description = "支付订单号", example = "Pxxx"),
            @Parameter(name = "sync", description = "是否同步", example = "true")
    })
    public CommonResult<PayOrderRespVO> getOder(@RequestParam(value = "id", required = false) Long id,
                                                @RequestParam(value = "no", required = false) String no,
                                                @RequestParam(value = "sync", required = false) Boolean sync) {
        PayOrderDO order = null;
        if (CharSequenceUtil.isNotEmpty(no)) {
            order = payOrderService.getOrder(no);
        }
        if (ObjUtil.isNull(order)&& ObjUtil.isNotNull(id)){
            order = payOrderService.getOrder(id);
        }
        if (order == null) {
            return success(null);
        }
        //校验订单是否是当前用户，避免越权
        if (order.getUserId()!=null&&ObjUtil.notEqual(order.getUserId(),getLoginUserId())){
            return success(null);
        }
        //sync 仅在等待支付
        if(Boolean.TRUE.equals(sync)&& PayOrderStatusEnum.isWaiting(order.getStatus())){
            payOrderService.syncOrderQuietly(order.getId());

            // 重新查询，因为同步后，可能会有变化
            order = payOrderService.getOrder(order.getId());
        }
        return null;
    }
}
