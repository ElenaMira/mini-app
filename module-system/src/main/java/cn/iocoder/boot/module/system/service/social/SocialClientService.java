package cn.iocoder.boot.module.system.service.social;


import me.zhyd.oauth.model.AuthUser;

/**
 * @author xiaosheng
 */
public interface SocialClientService {
    AuthUser getAuthUser(Integer socialType, Integer userType, String code, String state);
}
