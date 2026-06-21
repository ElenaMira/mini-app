package cn.iocoder.boot.module.pay.service.notify;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.dal.dataobject.notify.PayNotifyTaskDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.dal.mysql.notify.PayNotifyTaskMapper;
import cn.iocoder.boot.module.pay.enums.notify.PayNotifyTypeEnum;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.service.order.PayOrderService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author xiaosheng
 */
@Service
public class PayNotifyServiceImpl implements PayNotifyService {
    @Resource
    private PayNotifyTaskMapper payNotifyTaskMapper;

    @Resource
    private PayOrderService payOrderService;

    @Override
    public void createPayNotifyTask(Integer type, Long orderId) {
        PayNotifyTaskDO task = new PayNotifyTaskDO().setType(type).setDataId(orderId);
        task.setStatus(PayOrderStatusEnum.WAITING.getStatus())
                .setNextNotifyTime(LocalDateTime.now())
                .setNotifyTimes(0).setMaxNotifyTimes(PayNotifyTaskDO.NOTIFY_FREQUENCY.length+1);
        // 补充 appId + notifyUrl + merchant* 字段
        if (ObjectUtil.equal(task.getType(),PayNotifyTypeEnum.ORDER.getType())) {
            PayOrderDO order = payOrderService.getOrder(task.getDataId());
            task.setAppId(order.getAppId()).setNotifyUrl(order.getNotifyUrl())
                    .setMerchantOrderId(order.getMerchantOrderId());
        }
        // todo
//        else if (Objects.equals(task.getType(), PayNotifyTypeEnum.REFUND.getType())) {
//            PayRefundDO refund = refundService.getRefund(task.getDataId());
//            task.setAppId(refund.getAppId()).setNotifyUrl(refund.getNotifyUrl())
//                    .setMerchantOrderId(refund.getMerchantOrderId()).setMerchantRefundId(refund.getMerchantRefundId());
//        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.TRANSFER.getType())) {
//            PayTransferDO transfer = transferService.getTransfer(task.getDataId());
//            task.setAppId(transfer.getAppId()).setNotifyUrl(transfer.getNotifyUrl())
//                    .setMerchantTransferId(transfer.getMerchantTransferId());
//        }
        // 执行插入
        payNotifyTaskMapper.insert(task);

        //必须在事务提交后，在发起任务，否则 PayNotifyTaskDO 还没入库，就提前回调接入的业务
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 异步的原因：避免阻塞当前事务，无需等待结果
                getSelf().executeNotifyAsync(task);
            }
        });
    }
    /**
     * 异步执行单个支付通知
     *
     * @param task 通知任务
     */
    @Async
    public void executeNotifyAsync(PayNotifyTaskDO task) {
        executeNotify(task);
    }
    /**
     * 【加锁】执行单个支付通知
     *
     * @param task 通知任务
     */
    public void executeNotify(PayNotifyTaskDO task) {
        // 分布式锁，避免并发问题
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private PayNotifyServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
