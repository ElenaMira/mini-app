package cn.iocoder.boot.springsecurity.system.enums.sms;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.boot.springsecurity.common.enums.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@AllArgsConstructor
@Getter
public enum SmsSceneEnum implements ArrayValuable<Integer> {
    MEMBER_LOGIN(1, "user-sms-login", "会员用户 - 手机号登陆")

    ;
    /**
     * 使用场景编号
     */
    private final Integer scene;
    /**
     * 模板编码
     */
    private final String templateCode;
    /**
     * 场景描述
     */
    private final String description;

    private final static Integer[] ARRAY = Arrays.stream(values()).map(SmsSceneEnum::getScene).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAY;
    }
    public static SmsSceneEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }
}
