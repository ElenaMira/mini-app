package cn.iocoder.boot.springsecurity.system.service;

import cn.iocoder.boot.springsecurity.common.Object.BeanUtils;
import cn.iocoder.boot.springsecurity.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.boot.springsecurity.common.uitl.DataUtils;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2AccessTokenDO;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2RefreshTokenDO;
import cn.iocoder.boot.springsecurity.system.dal.mysql.OAuth2AccessTokenMapper;
import cn.iocoder.boot.springsecurity.system.dal.mysql.OAuth2RefreshTokenMapper;
import cn.iocoder.boot.springsecurity.system.dal.redis.OAuth2AccessTokenRedisDAO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.iocoder.boot.springsecurity.common.exception.util.ServiceExceptionUtil.exception0;

/**
 * @author xiaosheng
 */
@Service
public class OAuth2TokenServiceImpl implements OAuth2TokenService {
    @Resource
    private OAuth2AccessTokenRedisDAO oAuth2AccessTokenRedisDAO;

    @Resource
    private OAuth2AccessTokenMapper oAuth2AccessTokenMapper;

    @Resource
    private OAuth2RefreshTokenMapper oAuth2RefreshTokenMapper;

    @Override
    public OAuth2AccessTokenDO checkAccessToken(String accessToken) {
        //1. 检测accessToken
        OAuth2AccessTokenDO token = getAccessToken(accessToken);
        if(token==null){
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(),"访问令牌不存在");
        }
        if(DataUtils.isExpired(token.getExpiresTime())){
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(),"访问令牌过期");
        }

        return token;
    }

    @Override
    public OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes) {
            return null;
    }
    @Override
    public OAuth2AccessTokenDO getAccessToken(String accessToken){
        //1. 调用redisDAO
        OAuth2AccessTokenDO redisTokenDO = oAuth2AccessTokenRedisDAO.get(accessToken);
        if(redisTokenDO != null){
            return redisTokenDO;
        }
        // 2. 调用mysql获取
        OAuth2AccessTokenDO accessTokenDO = oAuth2AccessTokenMapper.selectByAccessToken(accessToken);
        if(accessTokenDO != null){
            //看看有没有刷新Token
            //可以直接用accessToken来替代
            OAuth2RefreshTokenDO refreshTokenDO = oAuth2RefreshTokenMapper.selectByRefreshToken(accessToken);
            if(refreshTokenDO != null&&!DataUtils.isExpired(refreshTokenDO.getExpiresTime())){
                accessTokenDO = convertToAccessToken(refreshTokenDO);
            }
        }
        if(accessTokenDO!=null && !DataUtils.isExpired(accessTokenDO.getExpiresTime())){
            oAuth2AccessTokenRedisDAO.set(accessTokenDO);
        }
        return accessTokenDO;
    }
    public OAuth2AccessTokenDO convertToAccessToken(OAuth2RefreshTokenDO oAuth2RefreshTokenDO){
        return BeanUtils.getBean(oAuth2RefreshTokenDO,OAuth2AccessTokenDO.class)
                .setAccessToken(oAuth2RefreshTokenDO.getRefreshToken());
    }
}
