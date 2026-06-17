package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp;

import lombok.extern.slf4j.Slf4j;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientConfig;



/**
 *
 *
 */
@Slf4j
public abstract class AbstractPayClient<Config extends PayClientConfig> implements PayClient<Config> {
    /**
     * 渠道编号
     */
    private final Long channelId;
    /**
     * 渠道编码
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final String channelCode;
    /**
     * 支付配置
     */
    protected Config config;

    public AbstractPayClient(Long channelId, String channelCode, Config config) {
        this.channelId = channelId;
        this.channelCode = channelCode;
        this.config = config;
    }

    public void init(){
        doInit();
        log.debug("[init][客户端({}) 初始化完成]", getId());
    }
    /**
     * 自定义初始化
     */
    protected abstract void doInit();
    @Override
    public Long getId() {
        return channelId;
    }

    /**
     * 刷新
     * @param config    对应的配置类
     */
    public final void refresh(Config config) {
        // 判断是否更新
        if (config.equals(this.config)) {
            return;
        }
        log.info("[refresh][客户端({})发生变化，重新初始化]", getId());
        this.config = config;
        // 初始化
        this.init();
    }

}
