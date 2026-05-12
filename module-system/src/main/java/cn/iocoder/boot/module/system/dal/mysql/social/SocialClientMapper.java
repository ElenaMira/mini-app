package cn.iocoder.boot.module.system.dal.mysql.social;

import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.module.system.dal.DO.social.SocialClientDO;
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
