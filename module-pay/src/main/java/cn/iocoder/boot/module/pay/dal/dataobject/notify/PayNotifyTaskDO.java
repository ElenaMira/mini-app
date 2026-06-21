package cn.iocoder.boot.module.pay.dal.dataobject.notify;

import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.enums.notify.PayNotifyTypeEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
@TableName("pay_notify_task")
@Data
public class PayNotifyTaskDO extends BaseDO {
    /**
     * 通知频率，单位为秒。
     *
     * 算上首次的通知，实际是一共 1 + 8 = 9 次。
     */
    public static final Integer[] NOTIFY_FREQUENCY = new Integer[]{
            15, 15, 30, 180,
            1800, 1800, 1800, 3600
    };

    /**
     * 编号，自增
     */
    @TableId
    private Long id;
    /**
     * 应用编号
     *
     * 关联 {@link PayAppDO#getId()}
     */
    private Long appId;
    /**
     * 通知类型
     *
     * 枚举 {@link PayNotifyTypeEnum}
     */
    private Integer type;
    /**
     * 数据编号，根据不同 type 进行关联：
     *
     * 1. {@link PayNotifyTypeEnum#ORDER} 时，关联 {@link PayOrderDO#getId()}
     * 2. {@link PayNotifyTypeEnum#REFUND} 时，关联 {@link PayRefundDO#getId()}
     * 3. {@link PayNotifyTypeEnum#TRANSFER} 时，关联 {@link PayTransferDO#getId()}
     */
    private Long dataId;

    /**
     * 商户订单编号
     */
    private String merchantOrderId;
    /**
     * 商户退款编号
     */
    private String merchantRefundId;
    /**
     * 商户转账编号
     */
    private String merchantTransferId;
    /**
     * 通知状态
     *
     * 枚举 {@link PayNotifyStatusEnum}
     */
    private Integer status;
    /**
     * 下一次通知时间
     */
    private LocalDateTime nextNotifyTime;
    /**
     * 最后一次执行时间
     */
    private LocalDateTime lastExecuteTime;
    /**
     * 当前通知次数
     */
    private Integer notifyTimes;
    /**
     * 最大可通知次数
     */
    private Integer maxNotifyTimes;
    /**
     * 通知地址
     */
    private String notifyUrl;
}
