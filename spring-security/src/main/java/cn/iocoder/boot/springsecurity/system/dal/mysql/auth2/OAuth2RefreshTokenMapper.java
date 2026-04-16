package cn.iocoder.boot.springsecurity.system.dal.mysql.auth2;

import cn.iocoder.boot.springsecurity.mybatis.mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth.OAuth2RefreshTokenDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface OAuth2RefreshTokenMapper extends BaseMapperX<OAuth2RefreshTokenDO> {
    default OAuth2RefreshTokenDO selectByRefreshToken(String refreshToken){
        return selectOne(OAuth2RefreshTokenDO::getRefreshToken, refreshToken);
    }
}
