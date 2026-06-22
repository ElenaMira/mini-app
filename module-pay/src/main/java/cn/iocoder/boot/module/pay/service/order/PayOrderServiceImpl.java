package cn.iocoder.boot.module.pay.service.order;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.util.number.MoneyUtils;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.boot.module.pay.dal.mysql.order.PayOrderExtensionMapper;
import cn.iocoder.boot.module.pay.dal.mysql.order.PayOrderMapper;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.enums.notify.PayNotifyTypeEnum;
import cn.iocoder.boot.module.pay.service.channel.PayChannelService;
import cn.iocoder.boot.module.pay.service.notify.PayNotifyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.boot.module.pay.enums.ErrorCodeConstants.*;

/**
 * @author xiaosheng
 */
@Slf4j
@Service
public class PayOrderServiceImpl implements PayOrderService {
    @Resource
    private PayOrderMapper  payOrderMapper;

    @Resource
    private PayOrderExtensionMapper payOrderExtensionMapper;

    @Resource
    private PayChannelService channelService;

    @Resource
    private PayNotifyService  payNotifyService;

    @Override
    public PayOrderDO getOrder(Long id) {
        return payOrderMapper.selectById(id);
    }

    @Override
    public PayOrderDO getOrder(String no) {
        return payOrderMapper.selectByNo(no);
    }

    @Override
    public void syncOrderQuietly(Long orderId) {
        //1. 查询待支付订单
        List<PayOrderExtensionDO> list = payOrderExtensionMapper.selectListByOrderIdAndStatus(orderId, PayOrderStatusEnum.WAITING.getStatus());

        //2. 遍历执行
        for (PayOrderExtensionDO payOrderExtensionDO : list) {
            syncOrder(payOrderExtensionDO);
        }
    }
    /**
     * 同步单个支付拓展单
     *
     * @param payOrderExtensionDO 支付拓展单
     * @return 是否已支付
     */
    private boolean syncOrder(PayOrderExtensionDO payOrderExtensionDO) {
        try{
            //1.1 查询支付Client
            PayClient<?> payClient = channelService.getPayClient(payOrderExtensionDO.getChannelId());
            if (null == payClient) {
                log.error("[syncOrder][渠道编号({}) 找不到对应的支付客户端]", payOrderExtensionDO.getChannelId());
                return false;
            }
            PayOrderRespDTO respDTO = payClient.getOrder(payOrderExtensionDO.getNo());

            if (PayOrderStatusEnum.isClosed(respDTO.getStatus())) {
                return false;
            }
            // 1.2 回调支付结果
            notifyOrder(payOrderExtensionDO.getChannelId(),respDTO);

            // 2. 如果是已支付，则返回 true
            return PayOrderStatusEnum.isSuccess(respDTO.getStatus());
        }catch (Throwable e){
            log.error("[syncOrder][orderExtension({}) 同步支付状态异常]", payOrderExtensionDO.getId(), e);
            return false;
        }
    }
    @Override
    public void notifyOrder(Long channelId, PayOrderRespDTO notify) {
        // 校验支付渠道是否有效
        PayChannelDO channel = channelService.validPayChannel(channelId);
        // 更新支付订单为已支付
        notifyOrder(channel,notify);


//        TenantUtils.execute(channel.getTenantId(), () -> getSelf().notifyOrder(channel, notify));
    }
    /**
     * 通知并更新订单的支付结果
     *
     * @param channel 支付渠道
     * @param notify  通知
     */
    @Transactional(rollbackFor = Exception.class)
    public void notifyOrder(PayChannelDO channel, PayOrderRespDTO notify) {
        // 情况一：支付成功的回调
        if (PayOrderStatusEnum.isSuccess(notify.getStatus())) {
            notifyOrderSuccess(channel, notify);
            return;
        }
        // 情况二：支付失败的回调
        if (PayOrderStatusEnum.isClosed(notify.getStatus())) {
            notifyOrderClosed(channel, notify);
        }

        // 情况三：WAITING：无需处理
        // 情况四：REFUND：通过退款回调处理
    }

    private void notifyOrderClosed(PayChannelDO channel, PayOrderRespDTO notify) {
        updateOrderExtensionClosed(channel, notify);
    }

    /**
     *  更新扩展单为关闭状态
     * @param channel   渠道
     * @param notify    请求扩展单
     */
    private void updateOrderExtensionClosed(PayChannelDO channel, PayOrderRespDTO notify) {
        // 1. 查询 PayOrderExtensionDO
        PayOrderExtensionDO orderExtension = payOrderExtensionMapper.selectByNo(notify.getOutTradeNo());
        if (orderExtension == null) {
            throw exception(PAY_ORDER_EXTENSION_NOT_FOUND);
        }
        if (PayOrderStatusEnum.isClosed(orderExtension.getStatus())) { // 如果已经是关闭，直接返回，不用重复更新
            log.info("[updateOrderExtensionClosed][orderExtension({}) 已经是支付关闭，无需更新]", orderExtension.getId());
            return;
        }
        // 一般出现先是支付成功，然后支付关闭，都是全部退款导致关闭的场景。这个情况，我们不更新支付拓展单，只通过退款流程，更新支付单
        if (PayOrderStatusEnum.isSuccess(orderExtension.getStatus())) {
            log.info("[updateOrderExtensionClosed][orderExtension({}) 是已支付，无需更新为支付关闭]", orderExtension.getId());
            return;
        }
        if (ObjectUtil.notEqual(orderExtension.getStatus(), PayOrderStatusEnum.WAITING.getStatus())) { // 校验状态，必须是待支付
            throw exception(PAY_ORDER_EXTENSION_STATUS_IS_NOT_WAITING);
        }

        // 2. 更新 PayOrderExtensionDO
        int updateCounts = payOrderExtensionMapper.updateByIdAndStatus(orderExtension.getId(), orderExtension.getStatus(),
                PayOrderExtensionDO.builder().status(PayOrderStatusEnum.CLOSED.getStatus()).channelNotifyData(toJsonString(notify))
                        .channelErrorCode(notify.getChannelErrorCode()).channelErrorMsg(notify.getChannelErrorMsg()).build());
        if (updateCounts == 0) { // 校验状态，必须是待支付
            throw exception(PAY_ORDER_EXTENSION_STATUS_IS_NOT_WAITING);
        }
        log.info("[updateOrderExtensionClosed][orderExtension({}) 更新为支付关闭]", orderExtension.getId());
    }

    private void notifyOrderSuccess(PayChannelDO channel, PayOrderRespDTO notify) {
        // 1. 更新 PayOrderExtensionDO 支付成功
        PayOrderExtensionDO orderExtension = updateOrderSuccess(notify);
        // 2. 更新 PayOrderDO 支付成功
        Boolean paid = updateOrderSuccess(channel,orderExtension,notify);
        // 如果之前已经成功回调，则直接返回，不用重复记录支付通知记录；例如说：支付平台重复回调
        if (paid) {
            return;
        }
        //3.插入支付通知记录
        payNotifyService.createPayNotifyTask(PayNotifyTypeEnum.ORDER.getType(),
                orderExtension.getOrderId());
    }

    /**
     * 更新 PayOrderExtensionDO 支付成功
     * @param notify    通知信息
     * @return 更新后的扩展单
     */
    private PayOrderExtensionDO updateOrderSuccess(PayOrderRespDTO notify) {
        //1. 查询PayOrderExtensionDO
        PayOrderExtensionDO payOrderExtensionDO = payOrderExtensionMapper.selectByNo(notify.getOutTradeNo());
        if (null == payOrderExtensionDO) {
            throw exception(PAY_ORDER_EXTENSION_NOT_FOUND);
        }
        // 如果已经是成功，直接返回，不用重复更新
        if (PayOrderStatusEnum.isSuccess(payOrderExtensionDO.getStatus())) {
            log.info("[updateOrderExtensionSuccess][orderExtension({}) 已经是已支付，无需更新]", payOrderExtensionDO.getId());
            return payOrderExtensionDO;
        }
        //校验扩展单必须为待支付状态
        if (ObjectUtil.notEqual(payOrderExtensionDO.getStatus(), PayOrderStatusEnum.WAITING.getStatus())) {
            throw exception(PAY_ORDER_EXTENSION_STATUS_IS_NOT_WAITING);
        }
        // 2. 更新 PayOrderExtensionDO
        int updateCounts = payOrderExtensionMapper.updateByIdAndStatus(payOrderExtensionDO.getId(),payOrderExtensionDO.getStatus(),
        PayOrderExtensionDO.builder()
                .status(PayOrderStatusEnum.SUCCESS.getStatus())
                .channelNotifyData(toJsonString(notify)).build());
        // 校验状态，必须是待支付
        if (updateCounts == 0) {
            throw exception(PAY_ORDER_EXTENSION_STATUS_IS_NOT_WAITING);
        }
        log.info("[updateOrderExtensionSuccess][orderExtension({}) 更新为已支付]", payOrderExtensionDO.getId());
        return payOrderExtensionDO;
    }

    /**
     * 基于扩展单更新PayOrderDO为支付成功
     * @param channel   渠道DO
     * @param extensionDO   扩展单
     * @param notify    通知单
     * @return  是否之前已经成功回调
     */
    private Boolean updateOrderSuccess(PayChannelDO channel,PayOrderExtensionDO extensionDO,PayOrderRespDTO notify) {
        //1. 判断 PayOrderDO 是否处于待支付
        PayOrderDO order = payOrderMapper.selectById(extensionDO.getOrderId());
        if (null == order) {
            throw exception(PAY_ORDER_NOT_FOUND);
        }
        // 如果已经是成功，直接返回，不用重复更新
        if (PayOrderStatusEnum.isSuccess(order.getStatus())
                && Objects.equals(order.getExtensionId(), extensionDO.getId())) {
            log.info("[updateOrderExtensionSuccess][order({}) 已经是已支付，无需更新]", order.getId());
            return true;
        }
        if (!PayOrderStatusEnum.WAITING.getStatus().equals(order.getStatus())) { // 校验状态，必须是待支付
            throw exception(PAY_ORDER_STATUS_IS_NOT_WAITING);
        }
        // 2. 更新 PayOrderDO
        int updateCounts = payOrderMapper.updateByIdAndStatus(order.getId(), PayOrderStatusEnum.WAITING.getStatus(),
                PayOrderDO.builder()
                        .status(PayOrderStatusEnum.SUCCESS.getStatus())
                        .channelId(channel.getId())
                        .channelCode(channel.getCode())
                        .successTime(notify.getSuccessTime()).extensionId(extensionDO.getId()).no(extensionDO.getNo())
                        .channelOrderNo(notify.getChannelOrderNo()).channelUserId(notify.getChannelUserId())
                        .channelFeeRate(channel.getFeeRate())
                        .channelFeePrice(MoneyUtils.calculateRatePrice(order.getPrice(), channel.getFeeRate()))
                        .build());
        if (updateCounts == 0) {
            throw exception(PAY_ORDER_STATUS_IS_NOT_WAITING);
        }
        log.info("[updateOrderExtensionSuccess][order({}) 更新为已支付]", order.getId());
        return false;
    }
}
