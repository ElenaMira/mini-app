package cn.iocoder.boot.module.system.dal.mysql.social;


import cn.iocoder.boot.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.boot.module.system.dal.DO.social.SocialUserBindDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
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

    default SocialUserBindDO selectByUserTypeAndSocialUserId(Integer userType, Long id){
        return selectOne(SocialUserBindDO::getUserType,userType,
                SocialUserBindDO::getUserId,id);
    }


    default SocialUserBindDO selectByUserTypeAndUserIdAndSocialType(Integer userType, Long loginUserId, Integer socialType) {
        return selectOne(new LambdaQueryWrapperX<SocialUserBindDO>()
                .eq(SocialUserBindDO::getUserType,userType)
                .eq(SocialUserBindDO::getUserId,loginUserId)
                .eq(SocialUserBindDO::getSocialType,socialType));
    }
}
