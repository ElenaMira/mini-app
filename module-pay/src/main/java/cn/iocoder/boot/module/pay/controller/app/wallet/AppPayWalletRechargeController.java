package cn.iocoder.boot.module.pay.controller.app.wallet;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletRechargeCreateReqVO;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletRechargeCreateRespVO;
import cn.iocoder.boot.module.pay.convert.wallet.PayWalletRechargeConvert;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.boot.module.pay.service.wallet.PayWalletRechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;
import static cn.iocoder.boot.web.web.core.util.WebFrameworkUtils.getLoginUserType;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 钱包充值")
@RestController
@RequestMapping("/pay/wallet-recharge")
@Slf4j
@Validated
public class AppPayWalletRechargeController {

    @Resource
    private PayWalletRechargeService walletRechargeService;


    @PostMapping("/create")
    @Operation(summary = "创建钱包充值记录（发起充值）")
    public CommonResult<AppPayWalletRechargeCreateRespVO> createWalletRecharge(
            @Valid @RequestBody AppPayWalletRechargeCreateReqVO reqVO) {
        PayWalletRechargeDO walletRecharge = walletRechargeService.createWalletRecharge(
                getLoginUserId(), getLoginUserType(), getClientIP(), reqVO);
        return success(PayWalletRechargeConvert.INSTANCE.convert(walletRecharge));
    }
}
