package cn.iocoder.boot.module.member.convert.address;

import cn.iocoder.boot.ip.core.utils.AreaUtils;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressRespVO;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.address.MemberAddressDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface AddressConvert {
    AddressConvert INSTANCE = Mappers.getMapper(AddressConvert.class);

    MemberAddressDO convert(AppAddressCreateReqVO reqVO);

    MemberAddressDO convert(AppAddressUpdateReqVO reqVO);

    @Mapping(source = "areaId", target = "areaName",  qualifiedByName = "convertAreaIdToAreaName")
    AppAddressRespVO convert(MemberAddressDO addressDO);

    @Named("convertAreaIdToAreaName")
    default String convertAreaIdToAreaName(Integer areaId) {
        return AreaUtils.format(areaId);
    }
}
