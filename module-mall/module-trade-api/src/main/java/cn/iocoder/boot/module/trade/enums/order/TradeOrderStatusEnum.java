package cn.iocoder.boot.module.trade.enums.order;

import cn.iocoder.boot.common.enums.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@RequiredArgsConstructor
@Getter
public enum TradeOrderStatusEnum implements ArrayValuable<Integer> {
    UNPAID(0, "待支付"),
    UNDELIVERED(10, "待发货"),
    DELIVERED(20, "已发货"),
    COMPLETED(30, "已完成"),
    CANCELED(40, "已取消");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TradeOrderStatusEnum::getStatus).toArray(Integer[]::new);


    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
