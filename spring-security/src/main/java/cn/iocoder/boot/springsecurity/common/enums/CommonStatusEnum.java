package cn.iocoder.boot.springsecurity.common.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements ArrayValuable<Integer>{

    ENABLE(0,"开启"),
    DISABLE(1,"关闭");

    /**
     *
     */
    private final Integer status;
    /**
     *
     */
    private final String name;

    private static final Integer[] ARRAYS = Arrays
            .stream(values())
            .map(CommonStatusEnum::getStatus)
            .toArray(Integer[]::new);

    public static boolean isDisable(Integer status) {
        return ObjUtil.equal(DISABLE.status, status);
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
