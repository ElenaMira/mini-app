package cn.iocoder.boot.springsecurity.member.vilidation;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.springsecurity.member.util.validationUtil.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author xiaosheng
 */
public class MobileValidator implements ConstraintValidator<Mobile,String> {
    @Override
    public void initialize(Mobile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 如果手机号为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return true;
        }
        // 校验手机
        return ValidationUtils.isMobile(value);
    }
}
