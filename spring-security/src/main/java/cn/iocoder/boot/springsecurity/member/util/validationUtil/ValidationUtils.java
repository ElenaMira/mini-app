package cn.iocoder.boot.springsecurity.member.util.validationUtil;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author xiaosheng
 */
public class ValidationUtils {
    // 1. 把手机号规则编译成 Pattern
    private static final Pattern PATTERN_MOBILE = Pattern.compile("^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[0,1,4-9])|(?:5[0-3,5-9])|(?:6[2,5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[0-3,5-9]))\\d{8}$");

    // 2. 使用方法
    public static boolean isMobile(String mobile){
        return StringUtils.hasText(mobile)          // 先判断不为空
                && PATTERN_MOBILE.matcher(mobile)   // 把字符串丢进去匹配
                .matches();                         // 返回 true/false
    }
}
