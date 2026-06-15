package cn.iocoder.boot.module.infra.service.file;

import cn.iocoder.boot.module.infra.framework.file.core.client.FileClient;

/**
 * @author xiaosheng
 */
public interface FileConfigService {
    /**
     * 获得 Master为true的 文件客户端
     *
     * @return 文件客户端
     */
    FileClient getMasterFileClient();
}
