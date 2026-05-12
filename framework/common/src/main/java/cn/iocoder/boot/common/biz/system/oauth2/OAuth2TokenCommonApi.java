package cn.iocoder.boot.common.biz.system.oauth2;

import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenBaseRespDTO;
import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import jakarta.validation.Valid;

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

    OAuth2AccessTokenBaseRespDTO createAccessToken(@Valid OAuth2AccessTokenCreateReqDTO reqDTO);

    OAuth2AccessTokenBaseRespDTO removeAccessToken(String token);

    OAuth2AccessTokenBaseRespDTO refreshToken(String refreshToken, String clientId);
}
