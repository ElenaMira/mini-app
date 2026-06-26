package cn.iocoder.boot.module.pay.controller.app.wallet;

import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletPackageRespVO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import cn.iocoder.boot.module.pay.service.wallet.PayWalletRechargePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

import static cn.iocoder.boot.common.pojo.CommonResult.success;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 钱包充值套餐")
@RestController
@RequestMapping("/pay/wallet-recharge-package")
@Validated
@Slf4j
public class AppPayWalletRechargePackageController {
    @Resource
    private PayWalletRechargePackageService walletRechargePackageService;

    @GetMapping("/list")
    @Operation(summary = "获得钱包充值套餐列表")
    public CommonResult<List<AppPayWalletPackageRespVO>> getWalletRechargePackageList() {
        List<PayWalletRechargePackageDO> list = walletRechargePackageService.getWalletRechargePackageList(
                CommonStatusEnum.ENABLE.getStatus());
        list.sort(Comparator.comparingInt(PayWalletRechargePackageDO::getPayPrice));
        return success(BeanUtils.toBean(list, AppPayWalletPackageRespVO.class));
    }
}
