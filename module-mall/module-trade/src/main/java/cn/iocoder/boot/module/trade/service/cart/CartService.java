package cn.iocoder.boot.module.trade.service.cart;

import cn.iocoder.boot.module.trade.controller.app.cart.vo.AppCartListRespVO;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface CartService {
    /**
     * 基于登录用户id获取购物车
     * @param loginUserId
     * @return
     */
    AppCartListRespVO getCartList(@NotNull Long loginUserId);
}
