package cn.iocoder.boot.module.infra.framework.file.core.client;

/**
 * @author xiaosheng
 */
public interface FileClientFactory {

    /**
     * 获得文件客户端
     *
     * @param configId 配置编号
     * @return 文件客户端
     */
    FileClient getFileClient(Long configId);


    /**
     * 创建or更新文件客户端
     * @param configId  配置对应的枚举ID
     * @param storage   对应的存储方式枚举
     * @param config    对应的配置类
     * @return  对应的客户端
     * @param <Config> 对应的配置类
     */
    <Config extends FileClientConfig> void createOrUpdateFileClient(Long configId, Integer storage,Config config);
}
