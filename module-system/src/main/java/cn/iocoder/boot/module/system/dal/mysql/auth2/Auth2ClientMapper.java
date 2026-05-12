package cn.iocoder.boot.module.system.dal.mysql.auth2;


import cn.iocoder.boot.module.system.dal.DO.OAuth.OAuth2ClientDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface Auth2ClientMapper extends BaseMapperX<OAuth2ClientDO> {
    default OAuth2ClientDO selectByClientId(String clientId){
        return selectOne(OAuth2ClientDO::getClientId,clientId);
    }
}
