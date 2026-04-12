package cn.iocoder.boot.springsecurity.system.service.oauth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.common.uitl.string.StrUtils;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2ClientDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.auth2.Auth2ClientMapper;
import cn.iocoder.boot.springsecurity.system.dal.redis.RedisKeyConstants;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.springsecurity.system.enums.ErrorCodeConstant.*;

/**
 * @author xiaosheng
 */
public class OAuth2ClientServiceImpl implements OAuth2ClientService{
    @Resource
    private Auth2ClientMapper auth2ClientMapper;
    @Override
    @Cacheable(cacheNames = RedisKeyConstants.OAUTH_CLIENT,key = "#clientId",unless = "#result == null")
    public OAuth2ClientDO getAuthClientFromCache(String clientId){
        return auth2ClientMapper.selectByClientId(clientId);
    }
    @Override
    public OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret, String authorizedGrantType,
                                                    Collection<String> scopes, String redirectUri) {
        OAuth2ClientDO client = getSelf().getAuthClientFromCache(clientId);
        if(client==null){
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
        if (CommonStatusEnum.isDisable(client.getStatus())) {
            throw exception(OAUTH2_CLIENT_DISABLE);
        }

        // 校验客户端密钥
        if (StrUtil.isNotEmpty(clientSecret) && ObjectUtil.notEqual(client.getSecret(), clientSecret)) {
            throw exception(OAUTH2_CLIENT_CLIENT_SECRET_ERROR);
        }
        // 校验授权方式
        if (StrUtil.isNotEmpty(authorizedGrantType) && !CollUtil.contains(client.getAuthorizedGrantTypes(), authorizedGrantType)) {
            throw exception(OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS);
        }
        // 校验授权范围
        if (CollUtil.isNotEmpty(scopes) && !CollUtil.containsAll(client.getScopes(), scopes)) {
            throw exception(OAUTH2_CLIENT_SCOPE_OVER);
        }
        // 校验回调地址
        if (StrUtil.isNotEmpty(redirectUri) && !StrUtils.startWithAny(redirectUri, client.getRedirectUris())) {
            throw exception(OAUTH2_CLIENT_REDIRECT_URI_NOT_MATCH, redirectUri);
        }
        return client;
    }
    private OAuth2ClientServiceImpl getSelf(){
        return SpringUtil.getBean(OAuth2ClientServiceImpl.class);
    }
}
