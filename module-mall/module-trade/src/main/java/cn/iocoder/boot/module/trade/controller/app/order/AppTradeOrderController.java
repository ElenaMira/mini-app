package cn.iocoder.boot.module.trade.controller.app.order;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.trade.enums.order.TradeOrderStatusEnum;
import cn.iocoder.boot.module.trade.service.afterSale.AfterSaleService;
import cn.iocoder.boot.module.trade.service.order.TradeOrderQueryService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 交易订单")
@RestController
@RequestMapping("/trade/order")
@Validated
@Slf4j
public class AppTradeOrderController {

    @Resource
    private TradeOrderQueryService tradeOrderQueryService;

    @Resource
    private AfterSaleService afterSaleService;

    @GetMapping("/get-count")
    @Operation(summary = "获得交易订单数量")
    public CommonResult<Map<String, Long>> getOrderCount() {
        LinkedHashMap<String,Long> orderCount = Maps.newLinkedHashMapWithExpectedSize(5);
        //全部
        orderCount.put("allCount",tradeOrderQueryService.getOrderCount(getLoginUserId(),null,null));
        //待付款(未付款)
        orderCount.put("unpaidCount",tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.UNPAID.getStatus(), null));
        // 待发货
        orderCount.put("undeliveredCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.UNDELIVERED.getStatus(), null));
        // 待收货
        orderCount.put("deliveredCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.DELIVERED.getStatus(), null));
        // 待评价
        orderCount.put("uncommentedCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.COMPLETED.getStatus(), false));
        //售后数量
        orderCount.put("afterSaleCount",afterSaleService.getApplyingAfterSaleCount(getLoginUserId()));
        return success(orderCount);
    }
}
