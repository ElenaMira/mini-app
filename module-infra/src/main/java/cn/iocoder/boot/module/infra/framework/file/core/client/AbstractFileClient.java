package cn.iocoder.boot.module.infra.framework.file.core.client;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaosheng
 */
@Slf4j
public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {
    /**
     * 配置编号
     */
    private final Long id;
    /**
     * 文件配置
     */
    protected Config config;

    /**
     * 原始的文件配置
     *
     * 原因：{@link #config} 可能被子类所修改，无法用于判断配置是否变更
     * @link <a href="https://t.zsxq.com/29wkW">相关案例</a>
     */
    private Config originalConfig;

    public AbstractFileClient(Long id ,Config config) {
        this.id = id;
        this.config = config;
        this.originalConfig = config;
    }

    public final void init(){
        doInit();
        log.debug("[init][配置({}) 初始化完成]", config);
    }

    /**
     * 自定义初始化
     */
    protected abstract void doInit();

    public final void refresh(Config config){
        //跳过未改变
        if(config.equals(this.originalConfig)){
            return;
        }
        log.info("[refresh][配置({})发生变化，重新初始化]", config);
        this.config = config;
        this.originalConfig = config;
        //初始化
        this.init();
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * 格式化文件地址
     *
     * @param domain 前缀地址
     * @param path 后缀地址
     * @return
     */
    protected String formatFileUtil(String domain,String path){
        //mac环境(注意mac文件名自带/User,因此这里file{}
//        return StrUtil.format("{}/admin-api/infra/file{}/get/{}",domain,getId(),path);
        //windows环境
        return StrUtil.format("{}/admin-api/infra/file/{}/get/{}",domain,getId(),path);
    }
}
