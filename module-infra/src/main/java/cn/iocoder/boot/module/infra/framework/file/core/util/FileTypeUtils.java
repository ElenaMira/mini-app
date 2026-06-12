package cn.iocoder.boot.module.infra.framework.file.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

/**
 * @author xiaosheng
 */
@Slf4j
public class FileTypeUtils {
    /**
     *
     */
    private static final Tika TIKA = new Tika();

    /**
     * 在拥有文件和数据的情况下，最好使用此方法，最为准确
     *
     * @param data 文件内容
     * @param name 文件名
     * @return mimeType 无法识别时会返回“application/octet-stream”
     */
    public static String getMimeType(byte[] data, String name){
        return TIKA.detect(data, name);
    }
    /**
     * 根据 mineType 获得文件后缀
     *
     * 注意：如果获取不到，或者发生异常，都返回 null
     *
     * @param mineType 类型
     * @return 后缀，例如说 .pdf
     */
    public static String getExtension(String mineType) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(mineType).getExtension()
        } catch (MimeTypeException e) {
            log.warn("[getExtension][获取文件后缀({}) 失败]", mineType, e);
            return null;
        }
    }
}
