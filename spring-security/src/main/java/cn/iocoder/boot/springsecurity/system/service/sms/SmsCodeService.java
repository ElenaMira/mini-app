package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeSendReqDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SmsCodeService {
    void sendSmsCode(@Valid SmsCodeSendReqDTO reqDTO);
}
