package cn.iocoder.boot.springsecurity.common.validation;

import cn.iocoder.boot.springsecurity.common.enums.ArrayValuable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaosheng
 */
public class InEnumValidator implements ConstraintValidator<InEnum,Integer> {
    //
    private List<?> values;

    /**
     *    校验枚举值,是否处于枚举范围
     * @param annotation 枚举对象
     */
    @Override
    public void initialize(InEnum annotation) {
        ArrayValuable<?>[] values = annotation.value().getEnumConstants();
        if (values.length == 0) {
            this.values = Collections.emptyList();
        } else {
            this.values = Arrays.asList(values[0].array());
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // 为空时，默认不校验，即认为通过
        if(value==null){
            return true;
        }
        // 校验通过
        if (values.contains(value)) {
            return true;
        }
        // 校验不通过，自定义提示语句
        context.disableDefaultConstraintViolation(); // 禁用默认的 message 的值
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
                .replaceAll("\\{value}", values.toString())).addConstraintViolation(); // 重新添加错误提示语句
        return false;
    }
}
