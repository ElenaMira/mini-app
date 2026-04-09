package cn.iocoder.boot.springsecurity.member.dal.mysql.user;

import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.springsecurity.mybatis.Mapper.BaseMapperX;
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
