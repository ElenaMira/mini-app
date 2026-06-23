package cn.iocoder.boot.module.pay.service.app;

import cn.iocoder.boot.module.pay.dal.dataobject.app.PayAppDO;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public interface PayAppService {
    PayAppDO validPayApp(@NotNull String appKey);
}
