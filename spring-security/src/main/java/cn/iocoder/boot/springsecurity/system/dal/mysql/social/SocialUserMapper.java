package cn.iocoder.boot.springsecurity.system.dal.mysql.social;

import cn.iocoder.boot.springsecurity.member.dal.dataObject.SocialUserDO;
import cn.iocoder.boot.springsecurity.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface SocialUserMapper extends BaseMapperX<SocialUserDO> {
   default SocialUserDO selectByTypeAndCodeAndState(Integer socialType,String code,String state){
       return  selectOne(SocialUserDO::getType,socialType
                ,SocialUserDO::getCode,code
                ,SocialUserDO::getState,state);
   }
    default  SocialUserDO selectByTypeAndOpenId(Integer socialType,String openId){
        return  selectFirst(SocialUserDO::getType,socialType,
                SocialUserDO::getOpenid,openId
        );
    }
}
