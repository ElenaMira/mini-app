package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface PayWalletService {
    PayWalletDO getOrCreateWallet(@NotNull Long loginUserId, Integer value);
}
