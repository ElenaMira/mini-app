package cn.iocoder.boot.module.system.api.social;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.module.system.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import cn.iocoder.boot.module.system.service.social.SocialClientService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class SocialClientApiImpl implements SocialClientApi {
    @Resource
    private SocialClientService socialClientService;


    //======================= 微信小程序独有 =======================
    @Override
    public SocialWxPhoneNumberInfoRespDTO getWxMaPhoneNumberInfo(Integer userType, String phoneCode) {
        WxMaPhoneNumberInfo info = socialClientService.getWxMaPhoneNumberInfo(userType, phoneCode);
        return BeanUtils.toBean(info, SocialWxPhoneNumberInfoRespDTO.class);
    }
}
