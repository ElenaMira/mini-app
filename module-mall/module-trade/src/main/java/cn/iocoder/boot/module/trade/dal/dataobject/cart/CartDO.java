package cn.iocoder.boot.module.trade.dal.dataobject.cart;

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName("trade_cart")
@Data
public class CartDO extends BaseDO {

    // ========= 基础字段 BEGIN =========

    /**
     * 编号，唯一自增
     */
    private Long id;

    /**
     * 用户编号
     *
     * 关联 MemberUserDO 的 id 编号
     */
    private Long userId;

    // ========= 商品信息 =========

    /**
     * 商品 SPU 编号
     *
     * 关联 ProductSpuDO 的 id 编号
     */
    private Long spuId;
    /**
     * 商品 SKU 编号
     *
     * 关联 ProductSkuDO 的 id 编号
     */
    private Long skuId;
    /**
     * 商品购买数量
     */
    private Integer count;
    /**
     * 是否选中
     */
    private Boolean selected;
}
