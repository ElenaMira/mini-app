package cn.iocoder.boot.module.member.dal.mysql.address;


import cn.iocoder.boot.module.member.dal.dataObject.app.address.MemberAddressDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface MemberAddressMapper extends BaseMapperX<MemberAddressDO> {

    default List<MemberAddressDO> selectByUserIdAndDefaulted(Long userId, Boolean defaulted){
        return  selectList(new LambdaQueryWrapperX<MemberAddressDO>()
                .eq(MemberAddressDO::getUserId,userId)
                .eqIfPresent(MemberAddressDO::getDefaultStatus,defaulted));
    }

    default MemberAddressDO selectByIdAndUserId(Long loginUserId, Long id){
        return selectOne(MemberAddressDO::getUserId, loginUserId, MemberAddressDO::getId, id);
    }
}
