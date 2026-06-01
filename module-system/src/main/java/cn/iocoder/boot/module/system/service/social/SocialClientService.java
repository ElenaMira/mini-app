package cn.iocoder.boot.module.system.service.social;


import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import me.zhyd.oauth.model.AuthUser;

/**
 * @author xiaosheng
 */
public interface SocialClientService {
    AuthUser getAuthUser(Integer socialType, Integer userType, String code, String state);

    /**
     * 获得微信小程序的手机信息
     *
     * @param userType  用户类型
     * @param phoneCode 手机授权码
     * @return 手机信息
     */
    WxMaPhoneNumberInfo getWxMaPhoneNumberInfo(Integer userType, String phoneCode);
}
