package org.example.module.pay.controller.app.order;

import cn.iocoder.boot.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.module.pay.controller.app.order.vo.PayOrderRespVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户 APP - 支付订单")
@RestController
@RequestMapping("/pay/order")
@Validated
@Slf4j
public class AppPayOrderControl {
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
//        PayOrderDO
        return null;
    }
}
