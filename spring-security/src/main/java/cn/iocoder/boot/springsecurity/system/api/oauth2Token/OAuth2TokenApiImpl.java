package cn.iocoder.boot.springsecurity.system.api.oauth2Token;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCreateRespDTO;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2AccessTokenDO;
import cn.iocoder.boot.springsecurity.system.service.OAuth2TokenService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service("OAuth2TokenApiImpl")
public class OAuth2TokenApiImpl implements OAuth2TokenCommonApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.checkAccessToken(accessToken);
        return BeanUtil.toBean(accessTokenDO, OAuth2AccessTokenCheckRespDTO.class);
    }

    @Override
    public OAuth2AccessTokenCreateRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(),
                reqDTO.getUserType(),
                reqDTO.getClientId(),
                reqDTO.getScopes()
        );
        return BeanUtil.toBean(accessTokenDO,OAuth2AccessTokenCreateRespDTO.class);
    }
}
