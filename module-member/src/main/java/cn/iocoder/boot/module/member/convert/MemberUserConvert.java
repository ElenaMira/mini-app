package cn.iocoder.boot.module.member.convert;

import cn.iocoder.boot.module.member.control.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.boot.module.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.module.member.dal.dataObject.level.MemberLevelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface MemberUserConvert {
    MemberUserConvert INSTANCE = Mappers.getMapper(MemberUserConvert.class);

    @Mappings({
            @Mapping(source = "level", target = "level"),
            @Mapping(source = "bean.id", target = "id"),
            @Mapping(source = "bean.experience", target = "experience")
    })
    AppMemberUserInfoRespVO convert(MemberUserDO bean, MemberLevelDO level);
}
