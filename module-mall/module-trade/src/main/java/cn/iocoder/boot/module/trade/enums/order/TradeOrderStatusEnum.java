package cn.iocoder.boot.module.trade.enums.order;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.enums.ArrayValuable;
import cn.iocoder.boot.common.util.object.ObjectUtils;
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
    UNDELIVERED(10, "待使用"),
    DELIVERED(20, "已使用"),
    COMPLETED(30, "已完成"),
    CANCELED(40, "已取消");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TradeOrderStatusEnum::getStatus).toArray(Integer[]::new);

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }


    /**
     * 判断指定状态，是否正处于【未付款】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean isUnpaid(Integer status) {
        return ObjectUtil.equal(UNPAID.getStatus(), status);
    }

    /**
     * 判断指定状态，是否正处于【待发货】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean isUndelivered(Integer status) {
        return ObjectUtil.equal(UNDELIVERED.getStatus(), status);
    }

    /**
     * 判断指定状态，是否正处于【已发货】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean isDelivered(Integer status) {
        return ObjectUtil.equals(status, DELIVERED.getStatus());
    }

    /**
     * 判断指定状态，是否正处于【已取消】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean isCanceled(Integer status) {
        return ObjectUtil.equals(status, CANCELED.getStatus());
    }

    /**
     * 判断指定状态，是否正处于【已完成】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean isCompleted(Integer status) {
        return ObjectUtil.equals(status, COMPLETED.getStatus());
    }

    /**
     * 判断指定状态，是否有过【已付款】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean havePaid(Integer status) {
        return ObjectUtils.equalsAny(status, UNDELIVERED.getStatus(),
                DELIVERED.getStatus(), COMPLETED.getStatus());
    }

    /**
     * 判断指定状态，是否有过【已发货】状态
     *
     * @param status 指定状态
     * @return 是否
     */
    public static boolean haveDelivered(Integer status) {
        return ObjectUtils.equalsAny(status, DELIVERED.getStatus(), COMPLETED.getStatus());
    }
}
