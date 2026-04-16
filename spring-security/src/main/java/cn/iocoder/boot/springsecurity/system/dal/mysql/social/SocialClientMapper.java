package cn.iocoder.boot.springsecurity.system.dal.mysql.social;

import cn.iocoder.boot.springsecurity.mybatis.mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.social.SocialClientDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface SocialClientMapper extends BaseMapperX<SocialClientDO> {
    default SocialClientDO selectBySocialTypeAndUserType(Integer socialType,Integer userType){
        return selectOne(SocialClientDO::getSocialType,socialType,SocialClientDO::getUserType,userType);
    }

}
