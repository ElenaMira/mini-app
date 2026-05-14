package cn.iocoder.boot.module.member.convert.address;

import cn.iocoder.boot.module.member.control.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.control.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.address.MemberAddressDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface AddressConvert {
    AddressConvert INSTANCE = Mappers.getMapper(AddressConvert.class);

    MemberAddressDO convert(AppAddressCreateReqVO reqVO);

    MemberAddressDO convert(AppAddressUpdateReqVO reqVO);
}
