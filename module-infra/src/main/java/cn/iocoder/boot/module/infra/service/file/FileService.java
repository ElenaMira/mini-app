package cn.iocoder.boot.module.infra.service.file;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author xiaosheng
 */
public interface FileService {
    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param content   文件内容
     * @param name      文件名称，允许空
     * @param directory 目录，允许空
     * @param type      文件的 MIME 类型，允许空
     * @return 文件路径
     */
    String createFile(@NotEmpty byte[] content,
                      String name, String directory, String type);

    /**
     *
     * @param configId 配置ID
     * @param path  文件相对路径
     * @return  文件UTF-8内容
     */
    byte[] getFileContent(Long configId, String path);
}
