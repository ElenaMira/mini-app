package cn.iocoder.boot.module.system.api.social;

import cn.iocoder.boot.module.system.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author xiaosheng
 */
public interface SocialClientApi {
    SocialWxPhoneNumberInfoRespDTO getWxMaPhoneNumberInfo(Integer userType, String phoneCode);
}
