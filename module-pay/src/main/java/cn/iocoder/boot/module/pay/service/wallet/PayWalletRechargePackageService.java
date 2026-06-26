package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;

import java.util.List;

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

    /**
     * 获取可用/不可用状态的套餐
     * @param status    是否可用
     * @return  套餐
     */
    List<PayWalletRechargePackageDO> getWalletRechargePackageList(Integer status);
}
