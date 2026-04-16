package cn.iocoder.boot.springsecurity.system.api.sms.dto;

import cn.iocoder.boot.springsecurity.common.validation.InEnum;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;
import cn.iocoder.boot.springsecurity.system.enums.sms.SmsSceneEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiaosheng
 */
@AllArgsConstructor
@Data
public class SmsCodeSendReqDTO {
    /**
     * 手机号
     */
    @Mobile
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    /**
     * 验证码发送场景
     */
    @NotNull(message = "场景不能为NULL")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;
    /**
     *发送的IP
     */
    @NotEmpty(message = "发送 IP 不能为空")
    private String createIp;
}
