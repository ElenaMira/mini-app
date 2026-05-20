package cn.iocoder.boot.module.member.dal.mysql.level;

import cn.iocoder.boot.module.member.convert.level.MemberLevelConvert;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface MemberLevelMapper extends BaseMapperX<MemberLevelDO> {
    default List<MemberLevelDO> selectListByStatus(Integer status){
        return selectList(new LambdaQueryWrapperX<MemberLevelDO>()
                .eq(MemberLevelDO::getStatus,status));
    }
}
