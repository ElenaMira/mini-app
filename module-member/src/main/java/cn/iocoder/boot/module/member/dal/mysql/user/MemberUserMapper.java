package cn.iocoder.boot.module.member.dal.mysql.user;

import cn.iocoder.boot.module.member.dal.dataObject.app.user.MemberUserDO;

import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface MemberUserMapper extends BaseMapperX<MemberUserDO> {

    default MemberUserDO selectByMobile(String mobile){
        return selectOne(MemberUserDO::getMobile,mobile);
    }

}
