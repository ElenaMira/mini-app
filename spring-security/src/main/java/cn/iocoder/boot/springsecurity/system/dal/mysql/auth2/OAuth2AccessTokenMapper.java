package cn.iocoder.boot.springsecurity.system.dal.mysql.auth2;

import cn.iocoder.boot.springsecurity.mybatis.mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth.OAuth2AccessTokenDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface OAuth2AccessTokenMapper extends BaseMapperX<OAuth2AccessTokenDO> {
    default OAuth2AccessTokenDO selectByAccessToken(String accessToken){
        return selectOne(OAuth2AccessTokenDO::getAccessToken, accessToken);
    }

}
