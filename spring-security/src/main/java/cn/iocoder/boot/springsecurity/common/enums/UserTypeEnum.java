package cn.iocoder.boot.springsecurity.common.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserTypeEnum implements ArrayValuable<Integer> {


    /**
     *  用俩个常量来替换魔法数字(1,2)从而增强代码的可读性和
     */
    MEMBER(1,"会员"),
    ADMIN(2,"管理员");

    /**
     *  用户类型
     *  枚举一般为final不可变
     */
    private final Integer value;

    /**
     *  用户类型名称
     */
    private final String name;

    /**
     * values()底层
     * public static final UserTypeEnum MEMBER = new UserTypeEnum(1, "会员");
     * public static final UserTypeEnum ADMIN = new UserTypeEnum(2, "管理员");
     *
     * public static UserTypeEnum[] values() {
     *     return new UserTypeEnum[] { MEMBER, ADMIN };
     * }
     *
     *  static final: 实现全局唯一共享常量
     */
    public static final Integer[] ARRAYS = Arrays.stream(values()).map(UserTypeEnum::getValue).toArray(Integer[]::new);

    /**
     * todo: 实现枚举参数检测
     * @param value  入参数字(根据数字返回对应的类型)
     * @return  根据数字返回对应的类型
     *
     *
     * userType: 遍历枚举中的每一项
     */

    public static UserTypeEnum valueOf(Integer value){
            return ArrayUtil.firstMatch(userType->userType.getValue().equals(value),UserTypeEnum.values());
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
