package cn.iocoder.boot.module.pay.dal.dataobject.wallet;

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName(value ="pay_wallet_transaction")
@Data
public class PayWalletTransactionDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 流水号
     */
    private String no;

    /**
     * 钱包编号
     *
     * 关联 {@link PayWalletDO#getId()}
     */
    private Long walletId;

    /**
     * 关联业务分类
     *
     * 枚举 {@link PayWalletBizTypeEnum#getType()}
     */
    private Integer bizType;

    /**
     * 关联业务编号
     */
    private String bizId;

    /**
     * 流水说明
     */
    private String title;

    /**
     * 交易金额，单位分
     *
     * 正值表示余额增加，负值表示余额减少
     */
    private Integer price;

    /**
     * 交易后余额，单位分
     */
    private Integer balance;
}
