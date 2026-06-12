package cn.iocoder.boot.module.infra.service.file;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.iocoder.boot.module.infra.dal.dataobject.FileDO;
import cn.iocoder.boot.module.infra.dal.mysql.FileMapper;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClient;
import cn.iocoder.boot.module.infra.framework.file.core.util.FileTypeUtils;
import com.baomidou.mybatisplus.extension.activerecord.AbstractModel;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import static cn.hutool.core.date.DatePattern.PURE_DATE_PATTERN;

/**
 * @author xiaosheng
 */
@Service
public class FileServiceImpl implements FileService {
    /**
     * 文件前缀生成器开关
     * 目的：保证文件的唯一性，避免覆盖
     */
    private static final boolean PATH_PREFIX_DATE_ENABLE = true;
    /**
     * 文件后缀生成器开关
     * 目的：保证文件的唯一性，避免覆盖
     */
    private static final boolean PATH_SUFFIX_TIMESTAMP_ENABLE = true;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileConfigService fileConfigService;

    @Override
    @SneakyThrows
    public String createFile(byte[] content, String name, String directory, String type) {
        if (StrUtil.isEmpty(type)){
            type = FileTypeUtils.getMimeType(content , name);
        }
        if (StrUtil.isEmpty(name)){
            name = DigestUtil.sha256Hex(content);
        }
        if (StrUtil.isEmpty(FileUtil.extName(name))){
            // 如果 name 没有后缀 type，则补充后缀
            String extension = FileTypeUtils.getExtension(type);
            if (StrUtil.isNotEmpty(extension)){
                name = name + extension;
            }
        }

        //生成唯一文件名
        String path = generateUploadPath(name,directory);
        // 2.2 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);
        // 3. 保存到数据库
        fileMapper.insert(new FileDO().setConfigId(client.getId())
                .setName(name).setPath(path).setUrl(url)
                .setType(type).setSize((long) content.length));
        return url;
    }

    @VisibleForTesting
    private String generateUploadPath(String name, String directory) {
        // 1. 生成前缀、后缀
        String prefix = null;
        if (PATH_PREFIX_DATE_ENABLE) {
            prefix = LocalDateTimeUtil.format(LocalDateTimeUtil.now(),PURE_DATE_PATTERN);
        }
        String suffix = null;
        if (PATH_SUFFIX_TIMESTAMP_ENABLE) {
            suffix = String.valueOf(System.currentTimeMillis());
        }
        // 2.1 先拼接 suffix 后缀
        if (StrUtil.isEmpty(suffix)){
            String extName = FileUtil.extName(name);
            if (StrUtil.isNotEmpty(extName)){
                name = FileUtil.mainName(name) + StrUtil.C_UNDERLINE + suffix+StrUtil.DOT + extName;
            }else {
                name = name + StrUtil.C_UNDERLINE + suffix;
            }
        }
        // 2.2 再拼接 prefix 前缀
        if (StrUtil.isNotEmpty(prefix)){
            name = prefix + StrUtil.C_UNDERLINE + name;
        }
        // 2.3 最后拼接 directory 目录
        if (StrUtil.isNotEmpty(directory)) {
            name = directory + StrUtil.SLASH + name;
        }
        return name;
    }
}
