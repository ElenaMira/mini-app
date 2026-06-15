package cn.iocoder.boot.module.infra.framework.file.core.client.local;

import cn.iocoder.boot.module.infra.framework.file.core.client.FileClientConfig;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

/**
 * @author xiaosheng
 */
@Data
public class LocalFileClientConfig implements FileClientConfig {
    /**
     * 基础路径
     */
    @NotEmpty(message = "基础路径不能为空")
    private String basePath;

    /**
     * 自定义域名
     */
    @NotEmpty(message = "domain 不能为空")
    @URL
    private String domain;
}
