package cn.iocoder.boot.module.system.api.oauth2Token;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.boot.common.biz.system.oauth2.OAuth2TokenCommonApi;

import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenBaseRespDTO;
import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.boot.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.boot.module.system.dal.DO.OAuth.OAuth2AccessTokenDO;
import cn.iocoder.boot.module.system.service.oauth.OAuth2TokenService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenCommonApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.checkAccessToken(accessToken);
        return BeanUtil.toBean(accessTokenDO, OAuth2AccessTokenCheckRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenBaseRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(),
                reqDTO.getUserType(),
                reqDTO.getClientId(),
                reqDTO.getScopes()
        );
        return BeanUtil.toBean(accessTokenDO, OAuth2AccessTokenBaseRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenBaseRespDTO removeAccessToken(String token) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        return BeanUtil.toBean(accessTokenDO, OAuth2AccessTokenBaseRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenBaseRespDTO refreshToken(String refreshToken, String clientId) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshToken(refreshToken, clientId);
        return BeanUtil.toBean(accessTokenDO,OAuth2AccessTokenBaseRespDTO.class);
    }
}
