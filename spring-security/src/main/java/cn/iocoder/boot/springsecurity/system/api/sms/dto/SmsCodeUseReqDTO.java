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
@Data
@AllArgsConstructor
public class SmsCodeUseReqDTO {

    /**
     * 手机号
     */
    @Mobile
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    /**
     * 发送场景
     */
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码")
    private String code;
    /**
     * 使用 IP
     */
    @NotEmpty(message = "使用 IP 不能为空")
    private String usedIp;
}
