package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionPageReqVO;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionSummaryRespVO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.boot.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.boot.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionPageReqVO.TYPE_EXPENSE;
import static cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionPageReqVO.TYPE_INCOME;

/**
 * @author xiaosheng
 */
@Service
public class PayWalletTransactionServiceImpl implements PayWalletTransactionService {
    @Resource
    private PayWalletService payWalletService;

    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(Long loginUserId, Integer userType, AppPayWalletTransactionPageReqVO pageVO) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(loginUserId, userType);
        return payWalletTransactionMapper.selectPage(wallet.getId(), pageVO.getType(), pageVO, pageVO.getCreateTime());
    }

    @Override
    public AppPayWalletTransactionSummaryRespVO getWalletTransactionSummary(Long loginUserId, Integer userType, LocalDateTime[] createTime) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(loginUserId, userType);
        return new AppPayWalletTransactionSummaryRespVO()
                .setTotalExpense(payWalletTransactionMapper.selectPriceSum(wallet.getId(), TYPE_EXPENSE, createTime))
                .setTotalIncome(payWalletTransactionMapper.selectPriceSum(wallet.getId(), TYPE_INCOME, createTime));
    }
}
