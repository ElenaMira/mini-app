package cn.iocoder.boot.module.system.service.oauth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.enums.UserTypeEnum;
import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.boot.common.uitl.DataUtils;
import cn.iocoder.boot.module.system.dal.DO.oauth.OAuth2AccessTokenDO;
import cn.iocoder.boot.module.system.dal.DO.oauth.OAuth2ClientDO;
import cn.iocoder.boot.module.system.dal.DO.oauth.OAuth2RefreshTokenDO;
import cn.iocoder.boot.module.system.dal.mysql.auth2.OAuth2AccessTokenMapper;
import cn.iocoder.boot.module.system.dal.mysql.auth2.OAuth2RefreshTokenMapper;
import cn.iocoder.boot.module.system.dal.redis.OAuth2AccessTokenRedisDAO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.  common.exception.util.ServiceExceptionUtil.exception0;
import static cn.iocoder.boot.  common.uitl.collection.CollectionUtils.convertSet;

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

    @Resource
    private OAuth2ClientService oAuth2ClientService;

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
        //基于客户端ID校验客户端模板
        OAuth2ClientDO oAuth2ClientDO = oAuth2ClientService.validOAuthClientFromCache(clientId);
        //创建刷新令牌
        OAuth2RefreshTokenDO refreshToken = createOAuth2RefreshToken(userId, userType, oAuth2ClientDO, scopes);
        //创建访问令牌
        return createOAuth2AccessToken(refreshToken,oAuth2ClientDO);
    }

    @Override
    public OAuth2AccessTokenDO removeAccessToken(String token) {
        OAuth2AccessTokenDO tokenDO = oAuth2AccessTokenMapper.selectByAccessToken(token);
        if (tokenDO == null){
            return null;
        }
        oAuth2AccessTokenMapper.deleteById(tokenDO.getId());
        oAuth2AccessTokenRedisDAO.delect(token);

        //删除刷新令牌
        oAuth2RefreshTokenMapper.deleteByRefreshToken(tokenDO.getRefreshToken());
        return tokenDO;
    }

    @Override
    @Transactional(noRollbackFor = ServiceException.class)
    public OAuth2AccessTokenDO refreshToken(String refreshToken, String clientId) {
        // 校验访问令牌
        OAuth2RefreshTokenDO refreshTokenDO = oAuth2RefreshTokenMapper.selectByRefreshToken(refreshToken);
        if (refreshTokenDO==null){
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "无效的刷新令牌");
        }

        // 校验 Client 匹配
        OAuth2ClientDO clientDO = oAuth2ClientService.validOAuthClientFromCache(clientId);
        if (ObjectUtil.notEqual(clientId,refreshTokenDO.getClientId())){
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "刷新令牌的客户端编号不正确");
        }
        //移除对应的access令牌
        List<OAuth2AccessTokenDO> accesstokenDOS = oAuth2AccessTokenMapper.selectListByRefreshToken(refreshToken);
        if (CollUtil.isNotEmpty(accesstokenDOS)){
            oAuth2AccessTokenMapper.deleteByIds(convertSet(accesstokenDOS,OAuth2AccessTokenDO::getId));
            oAuth2AccessTokenRedisDAO.delectList(convertSet(accesstokenDOS,OAuth2AccessTokenDO::getAccessToken));
        }

        //校验refresh过期时间
        if (DataUtils.isExpired(refreshTokenDO.getExpiresTime())){
            oAuth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "刷新令牌已过期");
        }

        //创建新的访问令牌
        return createOAuth2AccessToken(refreshTokenDO,clientDO);
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
            //看看有没有refreshToken
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
        return BeanUtils.toBean(oAuth2RefreshTokenDO,OAuth2AccessTokenDO.class)
                .setAccessToken(oAuth2RefreshTokenDO.getRefreshToken());
    }

    private OAuth2RefreshTokenDO createOAuth2RefreshToken(Long userId, Integer userType, OAuth2ClientDO clientDO, List<String> scopes){
        OAuth2RefreshTokenDO oAuth2RefreshTokenDO = new OAuth2RefreshTokenDO()
                .setRefreshToken(generateRefreshToken())
                .setUserId(userId)
                .setUserType(userType)
                .setClientId(clientDO.getClientId())
                .setScopes(scopes)
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getRefreshTokenValiditySeconds()));
        oAuth2RefreshTokenMapper.insert(oAuth2RefreshTokenDO);
        return oAuth2RefreshTokenDO;

    }

    private String generateRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }

    private OAuth2AccessTokenDO createOAuth2AccessToken(OAuth2RefreshTokenDO refreshTokenDO, OAuth2ClientDO clientDO){
        OAuth2AccessTokenDO accessTokenDO = new OAuth2AccessTokenDO().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDO.getUserId()).setUserType(refreshTokenDO.getUserType())
                .setUserInfo(buildUserInfo(refreshTokenDO.getUserId(), refreshTokenDO.getUserType()))
                .setClientId(clientDO.getClientId()).setScopes(refreshTokenDO.getScopes())
                .setRefreshToken(refreshTokenDO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(clientDO.getAccessTokenValiditySeconds()));

        oAuth2AccessTokenMapper.insert(accessTokenDO);
        oAuth2AccessTokenRedisDAO.set(accessTokenDO);
        return accessTokenDO;
    }
    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }
    /**
     * todo 完善管理员
     * 加载用户信息，方便 {@link } 获取到昵称、部门等信息
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @return 用户信息
     */
    private Map<String, String> buildUserInfo(Long userId, Integer userType) {
        if (userId == null || userId <= 0) {
            return Collections.emptyMap();
        }
        if (userType.equals(UserTypeEnum.ADMIN.getValue())) {
//            AdminUserDO user = adminUserService.getUser(userId);
//            return MapUtil.builder(LoginUser.INFO_KEY_NICKNAME, user.getNickname())
//                    .put(LoginUser.INFO_KEY_DEPT_ID, StrUtil.toStringOrNull(user.getDeptId())).build();
            return MapUtil.builder("nickname","zss").put("deptId","admin").build();
        } else if (userType.equals(UserTypeEnum.MEMBER.getValue())) {
            // 注意：目前 Member 暂时不读取，可以按需实现
            return Collections.emptyMap();
        }
        throw new IllegalArgumentException("未知用户类型：" + userType);
    }

}
