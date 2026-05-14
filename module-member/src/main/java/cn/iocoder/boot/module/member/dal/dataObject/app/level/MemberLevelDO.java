package cn.iocoder.boot.module.member.dal.dataObject.app.level;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName("member_level")
@Data
public class MemberLevelDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 等级名称
     */
    private String name;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 升级经验
     */
    private Integer experience;
    /**
     * 享受折扣
     */
    private Integer discountPercent;

    /**
     * 等级图标 todo
     */
    private String icon;
    /**
     * 等级背景图 todo
     */
    private String backgroundUrl;
    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
}
