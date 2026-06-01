package cn.iocoder.boot.module.member.convert.level;

import cn.iocoder.boot.module.member.controller.app.level.vo.AppMemberLevelRespVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface MemberLevelConvert {
    MemberLevelConvert INSTANCE = Mappers.getMapper(MemberLevelConvert.class);

    // 【必加】单个 DO → VO 映射方法
    AppMemberLevelRespVO convert(MemberLevelDO memberLevelDO);


    List<AppMemberLevelRespVO> convertList02(List<MemberLevelDO> result);
}
