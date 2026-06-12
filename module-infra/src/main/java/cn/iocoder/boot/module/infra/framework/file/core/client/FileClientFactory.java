package cn.iocoder.boot.module.infra.framework.file.core.client;

/**
 * @author xiaosheng
 */
public interface FileClientFactory {

    <Config extends FileClientConfig> createOrUpdateFileClient(Long configId, Integer storage,Config config);
}
