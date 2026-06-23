package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.common.util.date.DateUtils;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.boot.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.boot.module.pay.dal.redis.wallet.PayWalletLockRedisDAO;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
@Service
public class PayWalletServiceImpl implements PayWalletService {

    /**
     * 通知超时时间，单位：毫秒
     */
    public static final long UPDATE_TIMEOUT_MILLIS = 120 * DateUtils.SECOND_MILLIS;

    @Resource
    private PayWalletMapper payWalletMapper;

    @Resource
    private PayWalletLockRedisDAO payWalletLockRedisDAO;

    @Override
    @SneakyThrows
    public PayWalletDO getOrCreateWallet(Long loginUserId, Integer userType) {
        PayWalletDO payWalletDO = payWalletMapper.selectByUserIdAndType(loginUserId, userType);
        if (payWalletDO == null) {
            payWalletDO = payWalletLockRedisDAO.lock(loginUserId,UPDATE_TIMEOUT_MILLIS,()->{
                PayWalletDO newWallet = payWalletMapper.selectByUserIdAndType(loginUserId,userType);
                if (newWallet == null) {
                    newWallet = PayWalletDO.builder()
                            .userId(loginUserId)
                            .userType(userType)
                            .balance(0)
                            .freezePrice(0)
                            .totalExpense(0)
                            .totalRecharge(0)
                            .build();
                    newWallet.setCreateTime(LocalDateTime.now());
                    payWalletMapper.insert(newWallet);
                }
                return newWallet;
            });
        }
        return payWalletDO;
    }
}
