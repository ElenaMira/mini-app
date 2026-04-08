package cn.iocoder.boot.springsecurity.system.api.oauth2Token;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2AccessTokenDO;
import cn.iocoder.boot.springsecurity.system.service.OAuth2TokenService;
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
}
