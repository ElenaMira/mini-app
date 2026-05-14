package cn.iocoder.boot.module.member.service.address;

import cn.iocoder.boot.module.member.control.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.control.app.address.vo.AppAddressUpdateReqVO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface AddressService {

    Long createAddress(Long loginUserId, @Valid AppAddressCreateReqVO reqVO);

    void updateAddress(Long loginUserId, @Valid AppAddressUpdateReqVO updateReqVO);
}
