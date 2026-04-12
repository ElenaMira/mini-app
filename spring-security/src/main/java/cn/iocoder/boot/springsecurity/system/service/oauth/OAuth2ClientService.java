package cn.iocoder.boot.springsecurity.system.service.oauth;

import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2ClientDO;

import java.util.Collection;

/**
 * @author xiaosheng
 */
public interface OAuth2ClientService {
    default OAuth2ClientDO validOAuthClientFromCache(String cilentId){
        return validOAuthClientFromCache(cilentId,null,null,null,null);
    }

    OAuth2ClientDO getAuthClientFromCache(String clientId);


    OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret, String authorizedGrantType,
                                             Collection<String> scopes, String redirectUri);
}
