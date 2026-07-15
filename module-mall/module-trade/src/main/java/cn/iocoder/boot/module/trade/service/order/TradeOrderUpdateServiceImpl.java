package cn.iocoder.boot.module.trade.service.order;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.util.json.JsonUtils;
import cn.iocoder.boot.module.trade.dal.dataobject.TradeOrderDO;
import cn.iocoder.boot.module.trade.dal.mysql.order.TradeOrderMapper;
import cn.iocoder.boot.module.trade.enums.order.TradeOrderStatusEnum;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.module.trade.enums.ErrorCodeConstants.ORDER_NOT_FOUND;
import static cn.iocoder.boot.module.trade.enums.ErrorCodeConstants.ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR;

/**
 * @author xiaosheng
 */
@Service
@Slf4j
public class TradeOrderUpdateServiceImpl implements TradeOrderUpdateService {
    @Resource
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public void updateOrderPaid(Long id, Long payOrderId) {
        // 1.1 校验订单是否存在
        TradeOrderDO order = validateOrderExists(id);
        // 1.2 校验订单已支付
        if (!TradeOrderStatusEnum.isUnpaid(order.getStatus()) || order.getPayStatus()) {
            // 特殊：支付单号相同，直接返回，说明重复回调
            if (ObjectUtil.equals(order.getPayOrderId(), payOrderId)) {
                log.warn("[updateOrderPaid][order({}) 已支付，且支付单号相同({})，直接返回]", order, payOrderId);
                return;
            }
            log.error("[updateOrderPaid][order({}) 支付单不匹配({})，请进行处理！order 数据是：{}]",
                    id, payOrderId, JsonUtils.toJsonString(order));
            throw exception(ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR);
        }
    }

    @NotNull
    private TradeOrderDO validateOrderExists(Long id) {
        // 校验订单是否存在
        TradeOrderDO order = tradeOrderMapper.selectById(id);
        if (order == null) {
            throw exception(ORDER_NOT_FOUND);
        }
        return order;
    }
}
