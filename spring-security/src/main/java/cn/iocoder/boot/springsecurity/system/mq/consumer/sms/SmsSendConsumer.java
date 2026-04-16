package cn.iocoder.boot.springsecurity.system.mq.consumer.sms;

import cn.iocoder.boot.springsecurity.system.mq.message.sms.SmsSendMessage;
import cn.iocoder.boot.springsecurity.system.mq.producer.sms.SmsProducer;
import cn.iocoder.boot.springsecurity.system.service.sms.SmsSendService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author xiaosheng
 */
@Component
@Slf4j
public class SmsSendConsumer {
    @Resource
    private SmsSendService smsSendService;

    @EventListener //默认消费SmsSendMessage形参类型的数据
    @Async
    public void onMessage(SmsSendMessage message){
        log.info("[onMessage][消息内容({})]", message);
        smsSendService.doSendSms(message);
    }
}
