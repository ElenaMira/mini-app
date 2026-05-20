package cn.iocoder.boot.module.system.dal.mysql.auth2;


import cn.iocoder.boot.module.system.dal.DO.oauth.OAuth2AccessTokenDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface OAuth2AccessTokenMapper extends BaseMapperX<OAuth2AccessTokenDO> {
    default OAuth2AccessTokenDO selectByAccessToken(String accessToken){
        return selectOne(OAuth2AccessTokenDO::getAccessToken, accessToken);
    }


    default List<OAuth2AccessTokenDO> selectListByRefreshToken(String refreshToken) {
        return selectList(OAuth2AccessTokenDO::getRefreshToken, refreshToken);
    }
}
