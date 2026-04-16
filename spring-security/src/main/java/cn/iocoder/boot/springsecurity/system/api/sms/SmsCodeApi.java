package cn.iocoder.boot.springsecurity.system.api.sms;

import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeSendReqDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SmsCodeApi {
    void sendSmsCode(@Valid SmsCodeSendReqDTO reqDTO);
}
