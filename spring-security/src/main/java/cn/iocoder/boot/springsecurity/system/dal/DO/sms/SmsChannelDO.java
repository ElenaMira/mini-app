package cn.iocoder.boot.springsecurity.system.dal.DO.sms;

import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.system.dal.DO.BaseDO;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.enums.SmsChannelEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author xiaosheng
 */

@Data
@TableName(value = "system_sms_channel", autoResultMap = true)
@Builder
public class SmsChannelDO extends BaseDO {

    /**
     * 渠道编号
     */
    private Long id;
    /**
     * 短信签名
     */
    private String signature;
    /**
     * 渠道编码
     *
     * 枚举 {@link SmsChannelEnum}
     */
    private String code;
    /**
     * 启用状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 短信 API 的账号
     */
    private String apiKey;
    /**
     * 短信 API 的密钥
     */
    private String apiSecret;
    /**
     * 短信发送回调 URL
     */
    private String callbackUrl;
}
