package cn.iocoder.boot.module.infra.dal.dataobject;

import cn.iocoder.boot.module.infra.framework.file.core.client.FileClientConfig;
import cn.iocoder.boot.module.infra.framework.file.core.enums.FileStorageEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author xiaosheng
 */
@TableName(value = "infra_file_config", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileConfigDO extends BaseDO {
    /**
     * 配置编号，数据库自增
     */
    private Long id;
    /**
     * 配置名
     */
    private String name;
    /**
     * 存储器
     *
     * 枚举 {@link FileStorageEnum}
     */
    private Integer storage;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否为主配置
     *
     * 由于我们可以配置多个文件配置，默认情况下，使用主配置进行文件的上传
     */
    private Boolean master;

    /**
     * 支付渠道配置
     */
    @TableField(typeHandler = FileClientConfigTypeHandler.class)
    private FileClientConfig config;
}
