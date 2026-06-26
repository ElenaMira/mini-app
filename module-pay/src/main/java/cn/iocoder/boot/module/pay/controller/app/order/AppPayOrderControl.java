package cn.iocoder.boot.module.pay.controller.app.order;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderRespVO;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderSubmitRespVO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.enums.pay.PayChannelEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wallet.WalletPayClient;
import cn.iocoder.boot.module.pay.service.order.PayOrderService;
import cn.iocoder.boot.module.pay.service.wallet.PayWalletService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;
import static cn.iocoder.boot.web.web.core.util.WebFrameworkUtils.getLoginUserType;

@Tag(name = "用户 APP - 支付订单")
@RestController
@RequestMapping("/pay/order")
@Validated
@Slf4j
public class AppPayOrderControl {
    @Resource
    private PayOrderService payOrderService;

    @Resource
    private PayWalletService payWalletService;


    @GetMapping("/get")
    @Operation(summary = "获取支付订单")
    @Parameters({
            @Parameter(name = "id", description = "编号", example = "1024"),
            @Parameter(name = "no", description = "支付订单号", example = "Pxxx"),
            @Parameter(name = "sync", description = "是否同步", example = "true")
    })
    public CommonResult<AppPayOrderRespVO> getOder(@RequestParam(value = "id", required = false) Long id,
                                                   @RequestParam(value = "no", required = false) String no,
                                                   @RequestParam(value = "sync", required = false) Boolean sync) {
        PayOrderDO order = null;
        if (CharSequenceUtil.isNotEmpty(no)) {
            order = payOrderService.getOrder(no);
        }
        if (ObjUtil.isNull(order)&& ObjUtil.isNotNull(id)){
            order = payOrderService.getOrder(id);
        }
        if (order == null) {
            return success(null);
        }
        //校验订单是否是当前用户，避免越权
        if (order.getUserId()!=null&&ObjUtil.notEqual(order.getUserId(),getLoginUserId())){
            return success(null);
        }
        //sync 仅在等待支付
        if(Boolean.TRUE.equals(sync)&& PayOrderStatusEnum.isWaiting(order.getStatus())){
            payOrderService.syncOrderQuietly(order.getId());

            // 重新查询，因为同步后，可能会有变化
            order = payOrderService.getOrder(order.getId());
        }
        return success(BeanUtils.toBean(order, AppPayOrderRespVO.class));
    }
    @PostMapping("/submit")
    @Operation(summary = "提交支付订单")
    public CommonResult<AppPayOrderSubmitRespVO> submitPayOrder(@RequestBody AppPayOrderSubmitReqVO reqVO) {
        // 1. 钱包支付时，需要额外传 user_id 和 user_type
        if (Objects.equals(reqVO.getChannelCode(), PayChannelEnum.WALLET.getCode())) {
            if (reqVO.getChannelExtras() == null) {
                reqVO.setChannelExtras(Maps.newHashMapWithExpectedSize(1));
            }
            PayWalletDO wallet = payWalletService.getOrCreateWallet(getLoginUserId(), getLoginUserType());
            reqVO.getChannelExtras().put(WalletPayClient.WALLET_ID_KEY, String.valueOf(wallet.getId()));
        }

        // 2. 提交支付
        AppPayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, getClientIP());
        return success(BeanUtils.toBean(respVO, AppPayOrderSubmitRespVO.class));
    }
}
