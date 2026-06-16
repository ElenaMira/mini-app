package cn.iocoder.boot.module.infra.framework.file.core.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.util.http.HttpUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.IOException;

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
            return MimeTypes.getDefaultMimeTypes().forName(mineType).getExtension();
        } catch (MimeTypeException e) {
            log.warn("[getExtension][获取文件后缀({}) 失败]", mineType, e);
            return null;
        }
    }

    /**
     *
     * @param response
     * @param fileName
     * @param content
     */
    public static void writeAttachment(HttpServletResponse response, String fileName, byte[] content) throws IOException {
        //设置 header和contentType
        String mimeType = getMimeType(content,fileName);
        response.setContentType(mimeType);

        if(isImage(mimeType)){
            //
            response.setHeader("Content-Disposition", "inline;filename=" + HttpUtils.encodeUtf8(fileName));
        }else{
            response.setHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8(fileName));
        }
        // 针对 video 的特殊处理，解决视频地址在移动端播放的兼容性问题
        if (StrUtil.containsIgnoreCase(mimeType, "video")) {
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(content.length));
        }
        // 输出附件
        IoUtil.write(response.getOutputStream(), false, content);
    }

    /**
     *  判断是否为图片
     * @param mimeType mime类型
     * @return 是否
     */
    private static boolean isImage(String mimeType) {
        return StrUtil.startWith(mimeType,"image/");
    }
}
