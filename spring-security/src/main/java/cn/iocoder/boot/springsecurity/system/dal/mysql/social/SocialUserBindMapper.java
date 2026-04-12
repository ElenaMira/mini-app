package cn.iocoder.boot.springsecurity.system.dal.mysql.social;

import cn.iocoder.boot.springsecurity.mybatis.Mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.mybatis.Mapper.query.LambdaQueryWrapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.SocialUserBindDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface SocialUserBindMapper extends BaseMapperX<SocialUserBindDO> {

    default void deleteByUserTypeAndSocialUserId(Integer userType, Long socialUserId) {
        delete(new LambdaQueryWrapperX<SocialUserBindDO>()
                .eq(SocialUserBindDO::getUserType, userType)
                .eq(SocialUserBindDO::getSocialUserId, socialUserId));
    }
    default void deleteByUserTypeAndUserIdAndSocialType(Integer userType,Long userId,Integer socialType){
        delete(new LambdaQueryWrapperX<SocialUserBindDO>()
                .eq(SocialUserBindDO::getSocialType,socialType)
                .eq(SocialUserBindDO::getUserId,userId)
                .eq(SocialUserBindDO::getUserType,userType)
        );
    }

}
