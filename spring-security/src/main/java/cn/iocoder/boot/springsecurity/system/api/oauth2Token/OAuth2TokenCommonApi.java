package cn.iocoder.boot.springsecurity.system.api.oauth2Token;

import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCheckRespDTO;

/**
 * @author xiaosheng
 */
public interface OAuth2TokenCommonApi {
    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken);
}
