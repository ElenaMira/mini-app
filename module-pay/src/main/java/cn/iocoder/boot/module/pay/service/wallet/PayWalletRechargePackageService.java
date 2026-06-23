package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;

/**
 * @author xiaosheng
 */
public interface PayWalletRechargePackageService {
    /**
     *  校验充值套餐
     * @param packageId
     * @return
     */
    PayWalletRechargePackageDO validWalletRechargePackage(Long packageId);
}
