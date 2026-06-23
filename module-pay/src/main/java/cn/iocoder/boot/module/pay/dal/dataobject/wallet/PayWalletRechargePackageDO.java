package cn.iocoder.boot.module.pay.dal.dataobject.wallet;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName(value ="pay_wallet_recharge_package")
@Data
public class PayWalletRechargePackageDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 套餐名
     */
    private String name;

    /**
     * 支付金额
     */
    private Integer payPrice;
    /**
     * 赠送金额
     */
    private Integer bonusPrice;

    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
}
