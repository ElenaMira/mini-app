package cn.iocoder.boot.module.member.service.address;

import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.address.MemberAddressDO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface AddressService {

    Long createAddress(Long loginUserId, @Valid AppAddressCreateReqVO reqVO);

    void updateAddress(Long loginUserId, @Valid AppAddressUpdateReqVO updateReqVO);

    /**
     * 删除用户地址
     * @param loginUserId   用户编号
     * @param id    需要删除的编号
     */
    void deleteAddress(Long loginUserId, Long id);

    /**
     *  获取用户地址信息
     * @param loginUserId
     * @param id
     * @return
     */
    MemberAddressDO getAddress(Long loginUserId, Long id);

    /**
     *
     * @param loginUserId
     * @return
     */
    MemberAddressDO getDefaultUserAddress(Long loginUserId);
}
