package cn.iocoder.boot.module.pay.framework.pay.core.client;

public interface PayClient<Config> {

    /**
     * 获得渠道编号
     *
     * @return 渠道编号
     */
    Long getId();

    /**
     * 获得渠道配置
     *
     * @return 渠道配置
     */
    Config getConfig();

}
