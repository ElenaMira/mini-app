package cn.iocoder.boot.module.pay.dal.redis;

/**
 * @author xiaosheng
 */
public interface RedisKeyConstants {

    /**
     * 通知任务的分布式锁
     *
     * KEY 格式：pay_notify:lock:%d // 参数来自 DefaultLockKeyBuilder 类
     * VALUE 数据格式：HASH // RLock.class：Redisson 的 Lock 锁，使用 Hash 数据结构
     * 过期时间：不固定
     */
    String PAY_NOTIFY_LOCK = "pay_notify:lock:%d";

    String PAY_WALLET_LOCK = "pay_wallet:lock:%d";
}
