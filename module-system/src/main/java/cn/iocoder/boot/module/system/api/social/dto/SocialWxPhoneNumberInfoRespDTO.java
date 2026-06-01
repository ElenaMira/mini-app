package cn.iocoder.boot.module.system.api.social.dto;

import lombok.Data;

/**
 * @author xiaosheng
 */
@Data
public class SocialWxPhoneNumberInfoRespDTO {
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;
    /**
     * 区号
     */
    private String countryCode;
}
