package cn.iocoder.boot.module.pay.service.notify;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.common.util.json.JsonUtils;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import cn.iocoder.boot.module.pay.api.notify.dto.PayRefundNotifyReqDTO;
import cn.iocoder.boot.module.pay.api.notify.dto.PayTransferNotifyReqDTO;
import cn.iocoder.boot.module.pay.dal.dataobject.notify.PayNotifyLogDO;
import cn.iocoder.boot.module.pay.dal.dataobject.notify.PayNotifyTaskDO;
import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.module.pay.dal.mysql.notify.PayNotifyLogMapper;
import cn.iocoder.boot.module.pay.dal.mysql.notify.PayNotifyTaskMapper;
import cn.iocoder.boot.module.pay.dal.redis.notify.PayNotifyLockRedisDAO;
import cn.iocoder.boot.module.pay.enums.notify.PayNotifyStatusEnum;
import cn.iocoder.boot.module.pay.enums.notify.PayNotifyTypeEnum;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.service.order.PayOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.boot.common.util.date.DateUtils.addTime;
import static cn.iocoder.boot.module.pay.dal.redis.notify.PayNotifyLockRedisDAO.NOTIFY_TIMEOUT_MILLIS;

/**
 * @author xiaosheng
 */
@Service
@Slf4j
public class PayNotifyServiceImpl implements PayNotifyService {
    @Resource
    private PayNotifyTaskMapper payNotifyTaskMapper;

    @Resource
    @Lazy // 循环依赖，避免报错 todo优化
    private PayOrderService payOrderService;

    @Resource
    private PayNotifyLogMapper payNotifyLogMapper;

    @Resource
    private PayNotifyLockRedisDAO payNotifyLockRedisDAO;

    @Override
    public void createPayNotifyTask(Integer type, Long orderId) {
        PayNotifyTaskDO task = new PayNotifyTaskDO().setType(type).setDataId(orderId);
        task.setStatus(PayOrderStatusEnum.WAITING.getStatus())
                .setNextNotifyTime(LocalDateTime.now())
                .setNotifyTimes(0).setMaxNotifyTimes(PayNotifyTaskDO.NOTIFY_FREQUENCY.length+1);
        // 补充 appId + notifyUrl + merchant* 字段
        if (ObjectUtil.equal(task.getType(),PayNotifyTypeEnum.ORDER.getType())) {
            PayOrderDO order = payOrderService.getOrder(task.getDataId());
            task.setAppId(order.getAppId()).setNotifyUrl(order.getNotifyUrl())
                    .setMerchantOrderId(order.getMerchantOrderId());
        }
        // todo
//        else if (Objects.equals(task.getType(), PayNotifyTypeEnum.REFUND.getType())) {
//            PayRefundDO refund = refundService.getRefund(task.getDataId());
//            task.setAppId(refund.getAppId()).setNotifyUrl(refund.getNotifyUrl())
//                    .setMerchantOrderId(refund.getMerchantOrderId()).setMerchantRefundId(refund.getMerchantRefundId());
//        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.TRANSFER.getType())) {
//            PayTransferDO transfer = transferService.getTransfer(task.getDataId());
//            task.setAppId(transfer.getAppId()).setNotifyUrl(transfer.getNotifyUrl())
//                    .setMerchantTransferId(transfer.getMerchantTransferId());
//        }
        // 执行插入
        payNotifyTaskMapper.insert(task);

        //必须在事务提交后，在发起任务，否则 PayNotifyTaskDO 还没入库，就提前回调接入的业务
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 异步的原因：避免阻塞当前事务，无需等待结果
                getSelf().executeNotifyAsync(task);
            }
        });
    }
    /**
     * 异步执行单个支付通知
     *
     * @param task 通知任务
     */
    @Async
    public void executeNotifyAsync(PayNotifyTaskDO task) {
        executeNotify(task);
    }
    /**
     * 【加锁】执行单个支付通知
     *
     * @param task 通知任务
     */
    public void executeNotify(PayNotifyTaskDO task) {
        // 分布式锁，避免并发问题
        payNotifyLockRedisDAO.lock(task.getId(), NOTIFY_TIMEOUT_MILLIS,()->{
            PayNotifyTaskDO dbTask = payNotifyTaskMapper.selectById(task.getId());
            if (ObjectUtil.notEqual(task.getNotifyTimes(),dbTask.getNotifyTimes())) {
                log.warn("[executeNotifySync][task({}) 任务被忽略，原因是它的通知不是第 ({}) 次，可能是因为并发执行了]",
                        JsonUtils.toJsonString(task), dbTask.getNotifyTimes());
                return ;
            }
            //执行通知
            getSelf().executeNotify0(task);
        });
    }

    private void executeNotify0(PayNotifyTaskDO task) {
        //发起回调
        CommonResult<?> invokeResult = null;
        Throwable invokeException = null;
        try {
            invokeResult =executeNotifyInvoke(task);
        }catch (Throwable e){
            invokeException = e;
        }
        // 处理结果
        Integer newStatus = processNotifyResult(task, invokeResult, invokeException);
        // 记录 PayNotifyLog 日志
        String response = invokeException!=null? ExceptionUtil.getRootCauseMessage(invokeException):
            JsonUtils.toJsonString(invokeResult);
        payNotifyLogMapper.insert(PayNotifyLogDO.builder()
                        .taskId(task.getId())
                        .notifyTimes(task.getNotifyTimes()+1)
                        .status(newStatus)
                .response(response)
                .build());
    }

    /**
     * 处理并更新通知结果
     * @param task  任务
     * @param invokeResult  响应结果
     * @param invokeException   响应
     * @return  更新条数
     */
    private Integer processNotifyResult(PayNotifyTaskDO task, CommonResult<?> invokeResult, Throwable invokeException) {
        // 设置通用的更新 PayNotifyTaskDO 的字段
        PayNotifyTaskDO newTask = PayNotifyTaskDO.builder().id(task.getId())
                .lastExecuteTime(task.getLastExecuteTime())
                .notifyTimes(task.getNotifyTimes()).build();
        //情况一：调用成功
        if (invokeResult != null&&invokeResult.isSuccess()) {
            newTask.setStatus(PayNotifyStatusEnum.SUCCESS.getStatus());
            payNotifyTaskMapper.updateById(newTask);
            return newTask.getStatus();
        }

        // 情况二：调用失败、调用异常
        // 2.1 超过最大回调次数
        if (newTask.getNotifyTimes() >= PayNotifyTaskDO.NOTIFY_FREQUENCY.length) {
            newTask.setStatus(PayNotifyStatusEnum.FAILURE.getStatus());
            payNotifyTaskMapper.updateById(newTask);
            return newTask.getStatus();
        }
        // 2.2 未超过最大回调次数
        newTask.setNextNotifyTime(addTime(Duration.ofSeconds(PayNotifyTaskDO.NOTIFY_FREQUENCY[newTask.getNotifyTimes()])));
        newTask.setStatus(invokeException != null?PayNotifyStatusEnum.REQUEST_FAILURE.getStatus():
                PayNotifyStatusEnum.REQUEST_SUCCESS.getStatus());
        payNotifyTaskMapper.updateById(newTask);
        return newTask.getStatus();
    }

    /**
     * 执行单个支付任务的 HTTP 调用
     *
     * @param task 通知任务
     * @return HTTP 响应
     */
    private CommonResult<?> executeNotifyInvoke(PayNotifyTaskDO task) {
        //拼接 body 参数
        Object request;
        if (ObjectUtil.equals(task.getType(),PayNotifyTypeEnum.ORDER.getType())) {
            request = PayOrderNotifyReqDTO.builder().merchantOrderId(task.getMerchantOrderId())
                    .payOrderId(task.getDataId()).build();
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.REFUND.getType())) {
            request = PayRefundNotifyReqDTO.builder().merchantOrderId(task.getMerchantOrderId())
                    .merchantRefundId(task.getMerchantRefundId())
                    .payRefundId(task.getDataId()).build();
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.TRANSFER.getType())) {
            request = PayTransferNotifyReqDTO.builder().merchantTransferId(task.getMerchantTransferId())
                    .payTransferId(task.getDataId()).build();
        } else {
            throw new RuntimeException("未知的通知任务类型：" + JsonUtils.toJsonString(task));
        }
        // 拼接 header 参数(预留)
        Map<String, String> headers = new HashMap<>();

        // 发起请求
        try (HttpResponse response = HttpUtil.createPost(task.getNotifyUrl())
                .body(JsonUtils.toJsonString(request)).addHeaders(headers)
                .timeout((int)NOTIFY_TIMEOUT_MILLIS).execute()){
            // 解析结果
            return JsonUtils.parseObject(response.body(), CommonResult.class);
        }
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private PayNotifyServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
