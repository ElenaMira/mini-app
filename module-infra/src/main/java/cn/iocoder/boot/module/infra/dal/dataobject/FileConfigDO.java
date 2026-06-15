package cn.iocoder.boot.module.infra.dal.dataobject;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.util.json.JsonUtils;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClientConfig;
import cn.iocoder.boot.module.infra.framework.file.core.client.local.LocalFileClientConfig;
import cn.iocoder.boot.module.infra.framework.file.core.enums.FileStorageEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

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

    public static class FileClientConfigTypeHandler extends AbstractJsonTypeHandler<Object> {
        public FileClientConfigTypeHandler(Class<?> type, Field field) {
            super(type, field);
        }

        public FileClientConfigTypeHandler(Class<?> type) {
            super(type);
        }

        @Override
        public Object parse(String json) {
            FileClientConfig clientConfig = JsonUtils.parseObjectQuietly(json, new TypeReference<FileClientConfig>(){});
            if (clientConfig == null) {
                return null;
            }

            //兼容@Class存储
            String className = JsonUtils.parseObject(json,"@class",String.class);
            className = StrUtil.subAfter(className,".",true);
            switch (className) {
                //todo: 补全文件上传方法
//                case "DBFileClientConfig":
//                    return JsonUtils.parseObject2(json, DBFileClientConfig.class);
//                case "FtpFileClientConfig":
//                    return JsonUtils.parseObject2(json, FtpFileClientConfig.class);
                case "LocalFileClientConfig":
                    return JsonUtils.parseObject2(json, LocalFileClientConfig.class);
//                case "SftpFileClientConfig":
//                    return JsonUtils.parseObject2(json, SftpFileClientConfig.class);
//                case "S3FileClientConfig":
//                    return JsonUtils.parseObject2(json, S3FileClientConfig.class);
                default:
                    throw new IllegalArgumentException("未知的 FileClientConfig 类型：" + json);
            }
        }

        @Override
        public String toJson(Object obj) {
            return JsonUtils.toJsonString(obj);
        }
    }
}
