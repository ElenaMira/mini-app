package cn.iocoder.boot.module.pay.dal.dataobject.app;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author xiaosheng
 */
@TableName("pay_app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayAppDO extends BaseDO {

    /**
     * 应用编号，数据库自增
     */
    @TableId
    private Long id;
    /**
     * 应用标识
     */
    private String appKey;
    /**
     * 应用名
     */
    private String name;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 支付结果的回调地址
     */
    private String orderNotifyUrl;
    /**
     * 退款结果的回调地址
     */
    private String refundNotifyUrl;

    /**
     * 转账结果的回调地址
     */
    private String transferNotifyUrl;
}
