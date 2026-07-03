package cn.iocoder.boot.module.product.dal.dataObject.history;

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author xiaosheng
 */
@TableName("product_browse_history")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrowseHistoryDO extends BaseDO {

    /**
     * 记录编号
     */
    @TableId
    private Long id;
    /**
     * 商品 SPU 编号
     */
    private Long spuId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户是否删除
     */
    private Boolean userDeleted;
}
