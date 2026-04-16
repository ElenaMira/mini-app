package cn.iocoder.boot.springsecurity.system.dal.DO.sms;

import cn.iocoder.boot.springsecurity.system.dal.DO.BaseDO;
import cn.iocoder.boot.springsecurity.system.enums.sms.SmsSceneEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
@TableName(value = "system_sms_code")
@Data
@Builder
public class SmsCodeDO extends BaseDO {
    /**
     * 编号
     */
    private Long id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;
    /**
     * 发送场景
     *
     * 枚举 {@link SmsSceneEnum}
     */
    private Integer scene;
    /**
     * 创建 IP
     */
    private String createIp;
    /**
     * 今日发送的第几条
     */
    private Integer todayIndex;
    /**
     * 是否使用
     */
    private Boolean used;
    /**
     * 使用时间
     */
    private LocalDateTime usedTime;
    /**
     * 使用 IP
     */
    private String usedIp;

}
