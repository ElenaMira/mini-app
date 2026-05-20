package cn.iocoder.boot.module.member.service.address;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.boot.module.member.convert.address.AddressConvert;
import cn.iocoder.boot.module.member.dal.dataObject.app.address.MemberAddressDO;
import cn.iocoder.boot.module.member.dal.mysql.address.MemberAddressMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.module.member.enums.ErrorCodeConstants.ADDRESS_NOT_EXISTS;


/**
 * @author xiaosheng
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private MemberAddressMapper memberAddressMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAddress(Long loginUserId, AppAddressCreateReqVO reqVO) {
        //如果添加的是默认收件地址，则将原默认地址修改为非默认
        if (Boolean.TRUE.equals(reqVO.getDefaultStatus())){
            List<MemberAddressDO> addressDOList = memberAddressMapper.selectListByUserIdAndDefaulted(loginUserId, true);
            addressDOList.forEach(address->memberAddressMapper.updateById(MemberAddressDO.builder()
                            .id(address.getId())
                            .defaultStatus(false)
                    .build()));
        }

        // 插入新地址
        MemberAddressDO address = AddressConvert.INSTANCE.convert(reqVO);
        address.setUserId(loginUserId);
        memberAddressMapper.insert(address);
        return address.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(Long loginUserId, AppAddressUpdateReqVO reqVO) {
        //校验Address是否合法
        validAddressExists(loginUserId,reqVO.getId());
        //如果更新的是默认收件地址，则将原默认地址修改为非默认(排除自身)
        if (Boolean.TRUE.equals(reqVO.getDefaultStatus())){
            List<MemberAddressDO> addressDOS = memberAddressMapper.selectListByUserIdAndDefaulted(loginUserId, true);
            addressDOS.stream().filter(u->!u.getId().equals(reqVO.getId()))
                    .forEach(address->memberAddressMapper.updateById(MemberAddressDO.builder()
                            .id(address.getId())
                            .defaultStatus(false)
                            .build()));
        }
        //更新
        MemberAddressDO addressDO = AddressConvert.INSTANCE.convert(reqVO);
        memberAddressMapper.updateById(addressDO);
    }

    @Override
    public void deleteAddress(Long loginUserId, Long id) {
        //校验是否可以删除
        validAddressExists(loginUserId,id);

        //查询要删除的地址（判断是不是默认）
        MemberAddressDO deleteAddress = getAddress(loginUserId, id);

        // 删除
        memberAddressMapper.deleteById(id);

        // 如果删除的是【默认地址】，自动把剩下的第一个地址设为默认
        if (Boolean.TRUE.equals(deleteAddress.getDefaultStatus())) {
            List<MemberAddressDO> addressList = memberAddressMapper.selectListByUserId(loginUserId);
            if (CollUtil.isNotEmpty(addressList)) {
                MemberAddressDO newDefault = addressList.get(0);
                memberAddressMapper.updateById(
                        MemberAddressDO.builder()
                                .id(newDefault.getId())
                                .defaultStatus(true)
                                .build()
                );
            }
        }
    }
    @Override
    public MemberAddressDO getAddress(Long loginUserId,Long id){
        return memberAddressMapper.selectByIdAndUserId(loginUserId, id);
    }

    @Override
    public MemberAddressDO getDefaultUserAddress(Long loginUserId) {
        List<MemberAddressDO> memberAddressDOList = memberAddressMapper.selectListByUserIdAndDefaulted(loginUserId, true);
        return CollUtil.getFirst(memberAddressDOList);
    }

    @Override
    public List<MemberAddressDO> getAddressList(Long loginUserId) {
        return memberAddressMapper.selectListByUserId(loginUserId);
    }

    private void validAddressExists(Long loginUserId,  Long id) {
        MemberAddressDO addressDO = getAddress(loginUserId, id);
        if (addressDO == null) {
            throw exception(ADDRESS_NOT_EXISTS);
        }
    }
}
