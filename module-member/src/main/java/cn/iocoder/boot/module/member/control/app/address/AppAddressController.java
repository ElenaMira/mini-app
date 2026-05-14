package cn.iocoder.boot.module.member.control.app.address;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.control.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.control.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.boot.module.member.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 APP - 用户收件地址")
@RestController
@RequestMapping("/member/address")
public class AppAddressController {
    @Resource
    private AddressService addressService;

    @PostMapping("/create")
    @Operation(summary = "创建用户收件地址")
    public CommonResult<Long> createAddress(@RequestBody @Valid AppAddressCreateReqVO reqVO){
        return success(addressService.createAddress(getLoginUserId(),reqVO));
    }

    @PostMapping("/updata")
    @Operation(summary = "更新用户收件地址")
    public CommonResult<Boolean> updateAddress(@Valid @RequestBody AppAddressUpdateReqVO updateReqVO) {
        addressService.updateAddress(getLoginUserId(), updateReqVO);
        return success(true);
    }
}
