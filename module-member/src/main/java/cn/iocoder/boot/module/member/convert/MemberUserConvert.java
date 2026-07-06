package cn.iocoder.boot.module.member.convert;

import cn.iocoder.boot.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.user.MemberUserDO;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

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


    List<MemberUserRespDTO> convertList2(List<MemberUserDO> userListByNickname);
}
