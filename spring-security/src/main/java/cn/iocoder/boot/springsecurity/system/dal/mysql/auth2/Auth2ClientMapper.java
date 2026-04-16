package cn.iocoder.boot.springsecurity.system.dal.mysql.auth2;

import cn.iocoder.boot.springsecurity.mybatis.mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.OAuth.OAuth2ClientDO;
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
