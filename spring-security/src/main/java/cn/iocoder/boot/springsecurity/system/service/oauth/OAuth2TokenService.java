package cn.iocoder.boot.springsecurity.system.service.oauth;

import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth.OAuth2AccessTokenDO;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface OAuth2TokenService {

    /**
     * 基于Token检测是否存在令牌
     */
    OAuth2AccessTokenDO  checkAccessToken(String accessToken);
    /**
     *
     */
    OAuth2AccessTokenDO getAccessToken(String accessToken);
    /**
     * 创建访问令牌
     * 注意：该流程中，会包含创建刷新令牌的创建
     *
     * 参考 DefaultTokenServices 的 createAccessToken 方法
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param clientId 客户端编号
     * @param scopes 授权范围
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes);
}
