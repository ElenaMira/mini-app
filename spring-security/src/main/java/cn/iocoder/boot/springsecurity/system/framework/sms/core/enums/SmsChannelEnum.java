package cn.iocoder.boot.springsecurity.system.framework.sms.core.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsTemplateDO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
@Getter
@AllArgsConstructor
public enum SmsChannelEnum {
//    DEBUG_DING_TALK("DEBUG_DING_TALK", "调试(钉钉)"),
    ALIYUN("ALIYUN", "阿里云"),
//    TENCENT("TENCENT", "腾讯云"),
//    HUAWEI("HUAWEI", "华为云"),
//    QINIU("QINIU", "七牛云"),
    ;

    /**
     * 编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static SmsChannelEnum getByCode(String code){
        return ArrayUtil.firstMatch(o->o.getCode().equals(code),values());
    }
}
