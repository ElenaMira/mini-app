package cn.iocoder.boot.module.infra.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClient;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClientConfig;
import cn.iocoder.boot.module.infra.framework.file.core.client.local.LocalFileClient;
import cn.iocoder.boot.module.infra.framework.file.core.client.local.LocalFileClientConfig;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 文件枚举
 * @author xiaosheng
 */
@AllArgsConstructor
@Getter
public enum FileStorageEnum {
    LOCAL(10, LocalFileClientConfig.class, LocalFileClient.class),
    ;
    /**
     * 存储器
     */
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends FileClientConfig> configClass;

    /**
     * 客户端类
     */
    private final Class<? extends FileClient> clientClass;

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }

}
