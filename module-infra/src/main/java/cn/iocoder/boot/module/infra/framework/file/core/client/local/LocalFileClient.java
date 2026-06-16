package cn.iocoder.boot.module.infra.framework.file.core.client.local;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.module.infra.framework.file.core.client.AbstractFileClient;
import cn.iocoder.boot.module.infra.framework.file.core.client.FileClient;

import java.io.File;

/**
 * @author xiaosheng
 */
public class LocalFileClient extends AbstractFileClient<LocalFileClientConfig> {

    @Override
    protected void doInit() {
    }

    LocalFileClient(Long id,LocalFileClientConfig config) {
        super(id,config);
    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {
        String filePath = getFilePath(path);
        FileUtil.writeBytes(content, filePath);
        return super.formatFileUtil(config.getDomain(), path);
    }

    @Override
    public byte[] getContent(String path) {
        //获取绝对路径
        String filePath = getFilePath(path);
        try{
            return FileUtil.readBytes(filePath);
        }catch (Exception e){
            if(e.getMessage().startsWith("File not exist:")){
                return null;
            }
            throw e;
        }
    }

    private String getFilePath(String path) {
        return config.getBasePath() + File.separator + path;
    }
}
