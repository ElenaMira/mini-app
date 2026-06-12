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
    String createFile(@NotEmpty(message = "文件内容不能为空") byte[] content,
                      String name, String directory, String type);
}
