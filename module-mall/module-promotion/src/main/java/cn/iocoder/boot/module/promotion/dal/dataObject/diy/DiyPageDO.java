package cn.iocoder.boot.module.promotion.dal.dataObject.diy;

import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

/**
 * @author xiaosheng
 */
@TableName(value = "promotion_diy_page", autoResultMap = true)
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiyPageDO extends BaseDO {

    /**
     * 装修页面编号
     */
    @TableId
    private Long id;
    /**
     * 装修模板编号
     *
     * 关联 {@link DiyTemplateDO#getId()}
     */
    private Long templateId;
    /**
     * 页面名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 预览图，多个逗号分隔 todo: 后续添加
     */
//    @TableField(typeHandler = StringListTypeHandler.class)
//    private List<String> previewPicUrls;
    /**
     * 页面属性，JSON 格式
     */
    private String property;
}
