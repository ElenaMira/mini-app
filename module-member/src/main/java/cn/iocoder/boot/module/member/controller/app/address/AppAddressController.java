package cn.iocoder.boot.module.member.controller.app.address;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressRespVO;
import cn.iocoder.boot.module.member.controller.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.boot.module.member.convert.address.AddressConvert;
import cn.iocoder.boot.module.member.dal.dataObject.app.address.MemberAddressDO;
import cn.iocoder.boot.module.member.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/delete")
    @Operation(summary = "删除用户收件地址")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteAddress(@RequestParam("id") Long id) {
        addressService.deleteAddress(getLoginUserId(), id);
        return success(true);
    }
    @GetMapping("/get")
    @Operation(summary = "获得用户收件地址")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppAddressRespVO> getAddress(@RequestParam("id") Long id) {
        MemberAddressDO address = addressService.getAddress(getLoginUserId(), id);
        return success(AddressConvert.INSTANCE.convert(address));
    }
    @GetMapping("/get-default")
    @Operation(summary = "获得默认的用户收件地址")
    public CommonResult<AppAddressRespVO> getDefaultUserAddress() {
        MemberAddressDO address = addressService.getDefaultUserAddress(getLoginUserId());
        return success(AddressConvert.INSTANCE.convert(address));
    }
}
