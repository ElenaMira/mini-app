package cn.iocoder.boot.common.util.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;

/**
 * @author xiaosheng
 */
public class FileUtils {




    /**
     * 创建临时文件
     * 该文件会在 JVM 退出时，进行删除
     *
     * @param data 文件内容
     * @return 文件
     */
    @SneakyThrows
    public static File createTempFile(String data){
        File file = createTempFile();
        //写入内容
        FileUtil.writeUtf8String(data, file);
        return file;
    }

    /**
     * 创建临时文件
     * 该文件会在 JVM 退出时，进行删除
     *
     * @param data 文件内容
     * @return 文件
     */
    @SneakyThrows
    public static File createTempFile(byte[] data){
        File file = createTempFile();
        //写入内容
        FileUtil.writeBytes(data, file);
        return file;
    }
    /**
     * 创建临时文件，无内容
     * 该文件会在 JVM 退出时，进行删除
     *
     * @return 文件
     */
    @SneakyThrows
    public static File createTempFile() {
        File file = File.createTempFile(IdUtil.simpleUUID(), null);
        file.deleteOnExit();
        return file;
    }
}
