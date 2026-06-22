package cn.iocoder.boot.module.pay.dal.dataobject.notify;

import cn.iocoder.boot.module.pay.enums.notify.PayNotifyStatusEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author xiaosheng
 */
@TableName("pay_notify_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayNotifyLogDO extends BaseDO {

    /**
     * 日志编号，自增
     */
    private Long id;
    /**
     * 通知任务编号
     *
     * 关联 {@link PayNotifyTaskDO#getId()}
     */
    private Long taskId;
    /**
     * 第几次被通知
     *
     * 对应到 {@link PayNotifyTaskDO#getNotifyTimes()}
     */
    private Integer notifyTimes;
    /**
     * HTTP 响应结果
     */
    private String response;
    /**
     * 支付通知状态
     *
     * 枚举 {@link PayNotifyStatusEnum}
     */
    private Integer status;
}
