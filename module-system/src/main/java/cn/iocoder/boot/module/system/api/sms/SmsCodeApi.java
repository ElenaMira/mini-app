package cn.iocoder.boot.module.system.api.sms;

import cn.iocoder.boot.module.system.api.sms.dto.SmsCodeSendReqDTO;
import cn.iocoder.boot.module.system.api.sms.dto.SmsCodeUseReqDTO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface SmsCodeApi {
    void sendSmsCode(@Valid SmsCodeSendReqDTO reqDTO);

    /**
     *  使用验证码
     * @param reqDTO
     */
    void useSmsCode(@Valid SmsCodeUseReqDTO reqDTO);
}
