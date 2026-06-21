package cn.iocoder.boot.module.pay.service.notify;

/**
 * @author xiaosheng
 */
public interface PayNotifyService {
    /**
     * 创建回调通知任务
     *
     * @param type 订单类型
     * @param orderId 订单编号
     */
    void createPayNotifyTask(Integer type, Long orderId);
}
