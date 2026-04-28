package cn.iocoder.boot.springsecurity.system.api.sms;

import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeSendReqDTO;
import cn.iocoder.boot.springsecurity.system.api.sms.dto.SmsCodeUseReqDTO;
import cn.iocoder.boot.springsecurity.system.service.sms.SmsCodeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class SmsCodeApiImpl implements SmsCodeApi{
    @Resource
    private SmsCodeService smsCodeService;
    @Override
    public void sendSmsCode(SmsCodeSendReqDTO reqDTO) {
        smsCodeService.sendSmsCode(reqDTO);
    }

    @Override
    public void useSmsCode(SmsCodeUseReqDTO reqDTO) {
        smsCodeService.useSmsCode(reqDTO);
    }
}
