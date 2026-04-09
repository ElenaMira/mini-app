package cn.iocoder.boot.springsecurity.common.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@AllArgsConstructor
@Getter
public enum SocialTypeEnum implements ArrayValuable<Integer>{
    /**
     * 微信小程序
     *
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html">接入文档</a>
     */
    WECHAT_MINI_PROGRAM(34, "WECHAT_MINI_PROGRAM"),
    ;
    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型的标识
     */
    private final String source;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SocialTypeEnum::getType).toArray(Integer[]::new);
    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SocialTypeEnum valueOfType(Integer type){
        // 基于hutool工具实现对应Type的枚举返回
        return ArrayUtil
                .firstMatch(o->o.getType().equals(type),values());
    }
}
