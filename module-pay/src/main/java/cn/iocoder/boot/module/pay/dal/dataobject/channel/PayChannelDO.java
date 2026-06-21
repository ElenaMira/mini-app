package cn.iocoder.boot.module.pay.dal.dataobject.channel;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.util.json.JsonUtils;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientConfig;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx.WxPayClientConfig;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import lombok.*;
import com.fasterxml.jackson.core.type.TypeReference;
import java.lang.reflect.Field;

/**
 * @author xiaosheng
 */
@TableName(value = "pay_channel", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayChannelDO extends BaseDO {

    /**
     * 渠道编号，数据库自增
     */
    private Long id;
    /**
     * 渠道编码
     *
     * 枚举 {@link PayChannelEnum}
     */
    private String code;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 渠道费率，单位：百分比
     */
    private Double feeRate;
    /**
     * 备注
     */
    private String remark;

    /**
     * 应用编号
     *
     * 关联 {@link PayAppDO#getId()}
     */
    private Long appId;

    @TableField(typeHandler = PayClientConfigTypeHandler.class)
    private PayClientConfig config;

    public static class PayClientConfigTypeHandler extends AbstractJsonTypeHandler<Object> {
        public PayClientConfigTypeHandler(Class<?> type) {
            super(type);
        }

        public PayClientConfigTypeHandler(Class<?> type, Field field) {
            super(type, field);
        }

        @Override
        public Object parse(String json) {
            PayClientConfig payClientConfig = JsonUtils.parseObjectQuietly(json, new TypeReference<PayClientConfig>() {});
            if (payClientConfig == null) {
                return null;
            }

            // 兼容老版本的包路径
            String className = JsonUtils.parseObject(json, "@class", String.class);
            className = StrUtil.subAfter(className, ".", true);
            switch (className) {
                //todo:
//                case "AlipayPayClientConfig":
//                    return JsonUtils.parseObject2(json, AlipayPayClientConfig.class);
                case "WxPayClientConfig":
                    return JsonUtils.parseObject2(json, WxPayClientConfig.class);
//                case "NonePayClientConfig":
//                    return JsonUtils.parseObject2(json, NonePayClientConfig.class);
                default:
                    throw new IllegalArgumentException("未知的 PayClientConfig 类型：" + json);
            }
        }

        @Override
        public String toJson(Object obj) {
            return JsonUtils.toJsonString(obj);
        }
    }
}
