package cn.iocoder.boot.module.promotion.dal.dataObject.diy;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiaosheng
 */
@TableName(value = "promotion_diy_template", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiyTemplateDO {

    /**
     * 装修模板编号
     */
    @TableId
    private Long id;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 是否使用
     */
    private Boolean used;
    /**
     * 使用时间
     */
    private LocalDateTime usedTime;
    /**
     * 备注
     */
    private String remark;

    /**
     * 预览图
     */
//    @TableField(typeHandler = StringListTypeHandler.class)
//    private List<String> previewPicUrls;
    /**
     * uni-app 底部导航属性，JSON 格式
     */
    private String property;

}
