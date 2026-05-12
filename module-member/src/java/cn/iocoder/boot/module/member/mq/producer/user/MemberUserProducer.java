package cn.iocoder.boot.module.member.mq.producer.user;

import cn.iocoder.boot.springsecurity.common.message.user.MemberUserCreateMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author xiaosheng
 */
@Slf4j
@Component
public class MemberUserProducer {
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 发送 {@link MemberUserCreateMessage} 消息
     *
     * @param userId 用户编号
     */
    public void sendUserCreateMessage(Long userId) {
        applicationContext.publishEvent(new MemberUserCreateMessage().setUserId(userId));
    }
}
