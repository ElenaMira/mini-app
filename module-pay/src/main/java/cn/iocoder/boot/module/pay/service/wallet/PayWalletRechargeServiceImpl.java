package cn.iocoder.boot.module.pay.service.wallet;

import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.pay.api.order.PayOrderCreateReqDTO;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import cn.iocoder.boot.module.pay.dal.mysql.wallet.PayWalletRechargeMapper;
import cn.iocoder.boot.module.pay.framework.pay.config.PayProperties;
import cn.iocoder.boot.module.pay.service.order.PayOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Objects;

import static cn.iocoder.boot.common.util.date.DateUtils.addTime;
import static cn.iocoder.boot.module.pay.convert.wallet.PayWalletRechargeConvert.INSTANCE;

/**
 * @author xiaosheng
 */
@Service
@Validated
public class PayWalletRechargeServiceImpl implements PayWalletRechargeService {
    private static final String WALLET_RECHARGE_ORDER_SUBJECT = "钱包余额充值";
    @Resource
    private PayWalletRechargePackageService payWalletRechargePackageService;

    @Resource
    private PayWalletService payWalletService;

    @Resource
    private PayWalletRechargeMapper walletRechargeMapper;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private PayProperties payProperties;

    @Override
    public PayWalletRechargeDO createWalletRecharge(Long loginUserId, Integer loginUserType, String clientIP, AppPayWalletRechargeCreateReqVO reqVO) {
        // 1.1 计算充值金额
        int payPrice;
        int bonusPrice = 0;
        if (Objects.nonNull(reqVO.getPackageId())) {
            PayWalletRechargePackageDO rechargePackage = payWalletRechargePackageService.validWalletRechargePackage(reqVO.getPackageId());
            payPrice = rechargePackage.getPayPrice();
            bonusPrice = rechargePackage.getBonusPrice();
        } else {
            payPrice = reqVO.getPayPrice();
        }

        // 1.2 插入充值记录
        PayWalletDO wallet = payWalletService.getOrCreateWallet(loginUserId, loginUserType);
        PayWalletRechargeDO walletRecharge = INSTANCE.convert(wallet.getId(), payPrice, bonusPrice, reqVO.getPackageId());
        walletRechargeMapper.insert(walletRecharge);

        // 2.1 创建支付单
        Long payOrderId =payOrderService.createOrder(PayOrderCreateReqDTO.builder()
                        .userId(loginUserId).userType(loginUserType).userIp(clientIP)
                        .merchantOrderId(walletRecharge.getPayOrderId().toString()).subject(WALLET_RECHARGE_ORDER_SUBJECT)
                        .body("")
                        .price(walletRecharge.getPayPrice())
                        .appKey(payProperties.getWalletPayAppKey())
                        .expireTime(addTime(Duration.ofHours(2L))).build());

        // 2.2 更新钱包充值记录中支付订单 todo

        return null;
    }

    @Override
    public PageResult<PayWalletRechargeDO> getWalletRechargePackagePage(Long loginUserId, Integer userType, PageParam pageReqVO, Boolean payStatus) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(loginUserId, userType);
        return walletRechargeMapper.selectPage(pageReqVO, wallet.getId(), payStatus);
    }
}
