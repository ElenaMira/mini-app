package cn.iocoder.boot.module.reservation.enums.reservation;

import cn.iocoder.boot.common.enums.ArrayValuable;
import cn.iocoder.boot.module.system.enums.sms.SmsSceneEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@Getter
@AllArgsConstructor
public enum GymReserveStatusEnum implements ArrayValuable<Integer> {
    PENDING(1, "待使用"),
    CHECK_IN(2, "已核销"),
    CANCEL(3, "已取消"),
    MISS_APPOINT(4, "爽约失效");

    private final Integer code;
    private final String desc;

    private final static Integer[] ARRAY =  Arrays.stream(values()).map(GymReserveStatusEnum::getCode).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAY;
    }
}
