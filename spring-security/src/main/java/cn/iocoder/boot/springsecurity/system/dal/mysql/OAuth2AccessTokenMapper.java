package cn.iocoder.boot.springsecurity.system.dal.mysql;

import cn.iocoder.boot.springsecurity.mybatis.Mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2AccessTokenDO;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth2RefreshTokenDO;
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
