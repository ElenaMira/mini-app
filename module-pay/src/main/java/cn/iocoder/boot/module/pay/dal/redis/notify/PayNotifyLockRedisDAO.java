package cn.iocoder.boot.module.pay.dal.redis.notify;

import cn.iocoder.boot.common.util.date.DateUtils;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static cn.iocoder.boot.module.pay.dal.redis.RedisKeyConstants.PAY_NOTIFY_LOCK;

/**
 * @author xiaosheng
 */
@Repository
public class PayNotifyLockRedisDAO {
    /**
     * 通知超时时间，单位：秒
     */
    public static final int NOTIFY_TIMEOUT = 120;

    /**
     * {@link #NOTIFY_TIMEOUT} 的毫秒
     */
    public static final long NOTIFY_TIMEOUT_MILLIS = 120 * DateUtils.SECOND_MILLIS;
    @Resource
    private RedissonClient redissonClient;

    public void lock(Long id, Long timeoutMillis, Runnable runnable) {
        String lockKey = formatKey(id);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(timeoutMillis, TimeUnit.MILLISECONDS);
            // 执行逻辑
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    private static String formatKey(Long id) {
        return String.format(PAY_NOTIFY_LOCK, id);
    }
}
