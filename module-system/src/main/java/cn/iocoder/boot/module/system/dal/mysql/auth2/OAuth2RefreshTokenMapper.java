package cn.iocoder.boot.module.system.dal.mysql.auth2;

import cn.iocoder.boot.module.system.dal.DO.oauth.OAuth2RefreshTokenDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface OAuth2RefreshTokenMapper extends BaseMapperX<OAuth2RefreshTokenDO> {
    default OAuth2RefreshTokenDO selectByRefreshToken(String refreshToken){
        return selectOne(OAuth2RefreshTokenDO::getRefreshToken, refreshToken);
    }

    default int deleteByRefreshToken(String refreshToken){
        return delete(new LambdaQueryWrapperX<OAuth2RefreshTokenDO>()
                .eq(OAuth2RefreshTokenDO::getRefreshToken, refreshToken));
    }
}
