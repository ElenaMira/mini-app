package cn.iocoder.boot.module.pay.service.app;

import cn.iocoder.boot.module.pay.dal.dataobject.app.PayAppDO;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface PayAppService {
    /**
     * 校验PayApp
     * @param appKey
     * @return
     */
    PayAppDO validPayApp(@NotNull String appKey);

    /**
     * 支付应用的合法性
     *
     * @param appId 应用编号
     * @return 应用
     */
    PayAppDO validPayApp(Long appId);
}
