package cn.iocoder.boot.springsecurity.system.dal.DO.sms;

import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.system.dal.DO.BaseDO;
import cn.iocoder.boot.springsecurity.system.enums.sms.SmsTemplateTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * @author xiaosheng
 */
@Data
@TableName(value = "system_sms_template", autoResultMap = true)
public class SmsTemplateDO extends BaseDO {

    /**
     * 自增编号
     */
    private Long id;

    // ========= 模板相关字段 =========

    /**
     * 短信类型
     *
     * 枚举 {@link SmsTemplateTypeEnum}
     */
    private Integer type;
    /**
     * 启用状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 模板编码，保证唯一
     */
    private String code;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板内容
     *
     * 内容的参数，使用 {} 包括，例如: 您的验证码是{code}
     */
    private String content;
    /**
     * 参数数组(自动根据内容生成) 例如: {operation} 验证码：{code} -> params = ["operation", "code"]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> params;
    /**
     * 备注
     */
    private String remark;
    /**
     * 短信 API 的模板编号
     */
    private String apiTemplateId;

    // ========= 渠道相关字段 =========

    /**
     * 短信渠道编号
     *
     * 关联 {@link SmsChannelDO#getId()}
     */
    private Long channelId;
    /**
     * 短信渠道编码
     *
     * 冗余 {@link SmsChannelDO#getCode()}
     */
    private String channelCode;
}
