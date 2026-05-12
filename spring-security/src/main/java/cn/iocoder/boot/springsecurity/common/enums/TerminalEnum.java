package cn.iocoder.boot.springsecurity.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@AllArgsConstructor
@Getter
public enum TerminalEnum implements ArrayValuable<Integer>{
    UNKNOWN(0, "未知"), // 目的：在无法解析到 terminal 时，使用它
    WECHAT_MINI_PROGRAM(10, "微信小程序"),
    ;
    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TerminalEnum::getTerminal).toArray(Integer[]::new);

    /**
     * 终端
     */
    private final Integer terminal;
    /**
     * 终端名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
