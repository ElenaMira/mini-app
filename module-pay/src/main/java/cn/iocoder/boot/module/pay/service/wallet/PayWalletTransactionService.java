package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionPageReqVO;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionSummaryRespVO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
public interface PayWalletTransactionService {
    /**
     * 获取钱包余额信息
     * @param loginUserId   用户id
     * @param userType  用户类型
     * @param pageReqVO 分页参数
     * @return  钱包余额信息
     */
    PageResult<PayWalletTransactionDO> getWalletTransactionPage(@NotNull Long loginUserId, Integer userType, @Valid AppPayWalletTransactionPageReqVO pageReqVO);

    /**
     * 钱包流水统计
     * @param loginUserId
     * @param userType
     * @param createTime
     * @return  钱包流水统计
     */
    AppPayWalletTransactionSummaryRespVO getWalletTransactionSummary(@NotNull Long loginUserId, Integer userType, LocalDateTime[] createTime);
}
