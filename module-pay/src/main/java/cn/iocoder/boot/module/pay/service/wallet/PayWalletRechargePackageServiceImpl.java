package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import cn.iocoder.boot.module.pay.dal.mysql.wallet.PayWalletRechargePackageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.module.pay.enums.ErrorCodeConstants.WALLET_RECHARGE_PACKAGE_IS_DISABLE;
import static cn.iocoder.boot.module.pay.enums.ErrorCodeConstants.WALLET_RECHARGE_PACKAGE_NOT_FOUND;

/**
 * @author xiaosheng
 */
@Service
public class PayWalletRechargePackageServiceImpl implements PayWalletRechargePackageService {
    @Resource
    private PayWalletRechargePackageMapper walletRechargePackageMapper;

    @Override
    public PayWalletRechargePackageDO validWalletRechargePackage(Long packageId) {
        PayWalletRechargePackageDO rechargePackageDO = walletRechargePackageMapper.selectById(packageId);
        if (rechargePackageDO == null) {
            throw exception(WALLET_RECHARGE_PACKAGE_NOT_FOUND);
        }
        if (CommonStatusEnum.DISABLE.getStatus().equals(rechargePackageDO.getStatus())) {
            throw exception(WALLET_RECHARGE_PACKAGE_IS_DISABLE);
        }
        return rechargePackageDO;
    }
}
