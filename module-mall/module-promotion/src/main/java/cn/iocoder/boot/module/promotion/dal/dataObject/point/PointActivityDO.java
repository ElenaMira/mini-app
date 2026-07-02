package cn.iocoder.boot.module.promotion.dal.dataObject.point;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.fesod.sheet.annotation.ExcelProperty;

/**
 * @author xiaosheng
 */
@TableName(value = "promotion_point_activity", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PointActivityDO extends BaseDO {

    /**
     * 积分商城活动编号
     */
    @TableId
    private Long id;
    /**
     * 积分商城活动商品
     */
    private Long spuId;
    /**
     * 活动状态
     *
     * 枚举 {@link CommonStatusEnum 对应的类}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 积分商城活动库存(剩余库存积分兑换时扣减)
     */
    private Integer stock;
    /**
     * 积分商城活动总库存
     */
    private Integer totalStock;
}
