package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface PayWalletRechargeService {
    /**
     * 创建充值记录
     * @param loginUserId   登录用户ID
     * @param loginUserType 登录用户类型
     * @param clientIP  登录IP
     * @param reqVO 充值信息
     * @return  充值表数据
     */
    PayWalletRechargeDO createWalletRecharge(@NotNull Long loginUserId, Integer loginUserType, String clientIP, @Valid AppPayWalletRechargeCreateReqVO reqVO);
}
