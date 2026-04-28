package cn.iocoder.boot.springsecurity.system.service.sms;

import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeSendReqDTO;
import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeUseReqDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SmsCodeService {
    void sendSmsCode(@Valid SmsCodeSendReqDTO reqDTO);

    void useSmsCode(SmsCodeUseReqDTO reqDTO);
}
