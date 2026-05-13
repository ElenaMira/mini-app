package cn.iocoder.boot.module.system.dal.DO.sms;


import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.system.framework.sms.core.enums.SmsChannelEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaosheng
 */

@Data
@TableName(value = "system_sms_channel", autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
