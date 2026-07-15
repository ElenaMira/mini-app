package cn.iocoder.boot.module.trade.controller.app.cart;

import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.boot.module.trade.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.boot.common.pojo.CommonResult.success;
import static cn.iocoder.boot.springsecurity.core.uitl.SecurityUtils.getLoginUserId;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 购物车")
@RestController
@RequestMapping("/trade/cart")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AppCartController {
    @Resource
    private CartService cartService;

    @GetMapping("/list")
    @Operation(summary = "查询用户的购物车列表")
    public CommonResult<AppCartListRespVO> getCartList() {
        return success(cartService.getCartList(getLoginUserId()));
    }
}
