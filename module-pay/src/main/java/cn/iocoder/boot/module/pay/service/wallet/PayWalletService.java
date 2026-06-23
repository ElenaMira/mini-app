package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface PayWalletService {
    /**
     * 获取钱包信息,如果不存在则创建
     * @param loginUserId   用户id
     * @param userType  用户类型
     * @return
     */
    PayWalletDO getOrCreateWallet(@NotNull Long loginUserId, Integer userType);
}
