package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.pojo.PageResult;
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

    /**
     * 获取钱包充值分页记录
     * @param loginUserId   用户id
     * @param userType  用户类型
     * @param pageReqVO 分页参数
     * @param payStatus 是否支付
     * @return  钱包充值记录分页
     */
    PageResult<PayWalletRechargeDO> getWalletRechargePackagePage(@NotNull Long loginUserId, Integer userType, @Valid PageParam pageReqVO, Boolean payStatus);
}
