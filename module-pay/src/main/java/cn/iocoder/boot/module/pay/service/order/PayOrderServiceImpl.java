package cn.iocoder.boot.module.pay.service.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.boot.common.util.date.DateUtils;
import cn.iocoder.boot.common.util.number.MoneyUtils;
import cn.iocoder.boot.module.pay.api.order.PayOrderCreateReqDTO;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderSubmitReqVO;
import cn.iocoder.boot.module.pay.controller.app.order.vo.AppPayOrderSubmitRespVO;
import cn.iocoder.boot.module.pay.convert.order.PayOrderConvert;
import cn.iocoder.boot.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.boot.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.boot.module.pay.dal.mysql.order.PayOrderExtensionMapper;
import cn.iocoder.boot.module.pay.dal.mysql.order.PayOrderMapper;
import cn.iocoder.boot.module.pay.dal.redis.no.NoRedisDAO;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.framework.pay.config.PayProperties;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.enums.notify.PayNotifyTypeEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderUnifiedReqDTO;
import cn.iocoder.boot.module.pay.service.app.PayAppService;
import cn.iocoder.boot.module.pay.service.channel.PayChannelService;
import cn.iocoder.boot.module.pay.service.notify.PayNotifyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
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
@Validated
public class PayOrderServiceImpl implements PayOrderService {
    @Resource
    private PayOrderMapper  payOrderMapper;

    @Resource
    private PayOrderExtensionMapper payOrderExtensionMapper;

    @Resource
    private PayChannelService channelService;

    @Resource
    private PayNotifyService  payNotifyService;

    @Resource
    private PayAppService appService;

    @Resource
    private NoRedisDAO noRedisDAO;

    @Resource
    private PayProperties  payProperties;

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

    @Override
    public Long createOrder(PayOrderCreateReqDTO reqDTO) {
        // 校验 App
        PayAppDO appDO = appService.validPayApp(reqDTO.getAppKey());
        // 查询对应的支付交易单是否已经存在。如果是，则直接返回
        PayOrderDO order = payOrderMapper.selectByAppIdAndMerchantOrderId(appDO.getId(), reqDTO.getMerchantOrderId());
        if (Objects.nonNull(order)) {
            log.warn("[createOrder][appId({}) merchantOrderId({}) 已经存在对应的支付单({})]", order.getAppId(),
                    order.getMerchantOrderId(), toJsonString(order)); // 理论来说，不会出现这个情况
            return order.getId();
        }
        // 创建支付交易单
        order = PayOrderConvert.INSTANCE.convert(reqDTO).setAppId(appDO.getId())
                // 商户相关字段
                .setNotifyUrl(appDO.getOrderNotifyUrl())
                // 订单相关字段
                .setStatus(PayOrderStatusEnum.WAITING.getStatus())
                // 退款相关字段
                .setRefundPrice(0);
        payOrderMapper.insert(order);
        return order.getId();
    }

    @Override
    public AppPayOrderSubmitRespVO submitOrder(AppPayOrderSubmitReqVO reqVO, String userIp) {
        // 1.1 获得 PayOrderDO ，并校验其是否存在
        PayOrderDO order = validateOrderCanSubmit(reqVO.getId());

        // 1.2 校验支付渠道是否有效
        PayChannelDO channel = validateChannelCanSubmit(order.getAppId(), reqVO.getChannelCode());
        PayClient<?> client = channelService.getPayClient(channel.getId());

        // 2. 插入 PayOrderExtensionDO
        String no = noRedisDAO.generate(payProperties.getOrderNoPrefix());
        PayOrderExtensionDO orderExtension = PayOrderConvert.INSTANCE.convert(reqVO, userIp)
                .setOrderId(order.getId()).setNo(no)
                .setChannelId(channel.getId()).setChannelCode(channel.getCode())
                .setStatus(PayOrderStatusEnum.WAITING.getStatus());
        payOrderExtensionMapper.insert(orderExtension);

        // 3. 调用三方接口
        PayOrderUnifiedReqDTO unifiedOrderReqDTO = PayOrderConvert.INSTANCE.convert2(reqVO, userIp)
                //商家相关字段
                .setOutTradeNo(orderExtension.getNo()) // 注意，此处使用的是 PayOrderExtensionDO.no 属性！
                .setSubject(order.getSubject()).setBody(order.getBody())
                .setNotifyUrl(genChannelOrderNotifyUrl(channel))
                .setReturnUrl(reqVO.getReturnUrl())
                // 订单相关字段
                .setPrice(order.getPrice()).setExpireTime(order.getExpireTime());
        PayOrderRespDTO unifiedOrderResp = client.unifiedOrder(unifiedOrderReqDTO);
        // 4. 如果调用直接支付成功，则直接更新支付单状态为成功。例如说：付款码支付，免密支付时，就直接验证支付成功
        if (unifiedOrderResp != null) {
            try {
                getSelf().notifyOrder(channel, unifiedOrderResp);
            } catch (Exception e) {
                // 兼容 https://gitee.com/zhijiantianya/yudao-cloud/issues/I8SM9H 场景
                // 支付宝或微信扫码之后时，由于 PayClient 是直接返回支付成功，而支付也会有回调，导致存在并发更新问题，此时一般是可以 try catch 直接忽略
                log.warn("[submitOrder][order({}) channel({}) 支付结果({}) 通知时发生异常，可能是并发问题]",
                        order, channel, unifiedOrderResp, e);
            }
            // 如有渠道错误码，则抛出业务异常，提示用户
            if (StrUtil.isNotEmpty(unifiedOrderResp.getChannelErrorCode())) {
                throw exception(PAY_ORDER_SUBMIT_CHANNEL_ERROR, unifiedOrderResp.getChannelErrorCode(),
                        unifiedOrderResp.getChannelErrorMsg());
            }

            order = payOrderMapper.selectById(order.getId());
        }
        return PayOrderConvert.INSTANCE.convert(order, unifiedOrderResp);
    }

    private PayOrderServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

    @Override
    public List<PayOrderDO> getOrderList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return payOrderMapper.selectByIds(ids);
    }

    /**
     * 校验支付渠道
     * @param appId
     * @param channelCode
     * @return
     */
    private PayChannelDO validateChannelCanSubmit(Long appId,String channelCode) {
        // 校验 App
        appService.validPayApp(appId);
        // 校验支付渠道是否有效
        PayChannelDO channel = channelService.validPayChannel(appId, channelCode);
        PayClient<?> client = channelService.getPayClient(channel.getId());
        if (client == null) {
            log.error("[validatePayChannelCanSubmit][渠道编号({}) 找不到对应的支付客户端]", channel.getId());
            throw exception(CHANNEL_NOT_FOUND);
        }
        return channel;
    }

    private PayOrderDO validateOrderCanSubmit(Long id) {
        PayOrderDO order = payOrderMapper.selectById(id);
        if (order == null) { // 是否存在
            throw exception(PAY_ORDER_NOT_FOUND);
        }
        if (PayOrderStatusEnum.isSuccess(order.getStatus())) { // 校验状态，发现已支付
            throw exception(PAY_ORDER_STATUS_IS_SUCCESS);
        }
        if (!PayOrderStatusEnum.WAITING.getStatus().equals(order.getStatus())) { // 校验状态，必须是待支付
            throw exception(PAY_ORDER_STATUS_IS_NOT_WAITING);
        }
        if (DateUtils.isExpired(order.getExpireTime())) { // 校验是否过期
            throw exception(PAY_ORDER_IS_EXPIRED);
        }
        // 【重要】校验是否支付拓展单已支付，只是没有回调、或者数据不正常
        validateOrderActuallyPaid(id);
        return order;
    }
    /**
     * 校验支付订单实际已支付
     *
     * @param id 支付编号
     */
    private void validateOrderActuallyPaid(Long id) {
        List<PayOrderExtensionDO> extensionList = payOrderExtensionMapper.selectListByOrderId(id);
        for (PayOrderExtensionDO orderExtension : extensionList) {
            // 校验扩展单状态
            if (PayOrderStatusEnum.isSuccess(orderExtension.getStatus())) {
                log.warn("[validateOrderCanSubmit][order({}) 的 extension({}) 已支付，可能是数据不一致]",
                        id, orderExtension.getId());
                throw exception(PAY_ORDER_EXTENSION_IS_PAID);
            }
            //调用第三方接口查状态
            PayClient<?> payClient = channelService.getPayClient(orderExtension.getChannelId());
            if (payClient == null) {
                log.error("[validateOrderCanSubmit][渠道编号({}) 找不到对应的支付客户端]", orderExtension.getChannelId());
                return;
            }
            PayOrderRespDTO respDTO = payClient.getOrder(orderExtension.getNo());
            if (respDTO != null && PayOrderStatusEnum.isSuccess(respDTO.getStatus())) {
                log.warn("[validateOrderCanSubmit][order({}) 的 PayOrderRespDTO({}) 已支付，可能是回调延迟]",
                        id, toJsonString(respDTO));
                throw exception(PAY_ORDER_EXTENSION_IS_PAID);
            }
        }

    }

    /**
     * 根据支付渠道的编码，生成支付渠道的回调地址
     *
     * @param channel 支付渠道
     * @return 支付渠道的回调地址  配置地址 + "/" + channel id
     */
    private String genChannelOrderNotifyUrl(PayChannelDO channel) {
        return payProperties.getOrderNotifyUrl() + "/" + channel.getId();
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
