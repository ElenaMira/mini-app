package cn.iocoder.boot.module.system.api.social;

import cn.iocoder.boot.module.system.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author xiaosheng
 */
public interface SocialClientApi {
    /**
     * 获得微信小程序的手机信息
     *
     * @param userType  用户类型
     * @param phoneCode 手机授权码
     * @return 手机信息
     */
    SocialWxPhoneNumberInfoRespDTO getWxMaPhoneNumberInfo(Integer userType, String phoneCode);
}
