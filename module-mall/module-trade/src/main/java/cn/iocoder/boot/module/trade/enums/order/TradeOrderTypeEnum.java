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
public enum TradeOrderTypeEnum implements ArrayValuable<Integer> {
    NORMAL(0, "普通订单"),
    SECKILL(1, "秒杀订单"),
    BARGAIN(2, "砍价订单"),
    COMBINATION(3, "拼团订单"),
    POINT(4, "积分商城"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TradeOrderTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
