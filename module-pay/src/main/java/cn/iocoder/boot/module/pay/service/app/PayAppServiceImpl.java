package cn.iocoder.boot.module.pay.service.app;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.boot.module.pay.dal.mysql.app.PayAppMapper;
import cn.iocoder.boot.module.pay.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author xiaosheng
 */
@Service
@Validated
public class PayAppServiceImpl implements PayAppService {

    @Resource
    private PayAppMapper appMapper;

    @Override
    public PayAppDO validPayApp(String appKey) {
        PayAppDO app = appMapper.selectByAppKey(appKey);
        return validatePayApp(app);
    }

    private PayAppDO validatePayApp(PayAppDO app) {
        // 校验是否存在
        if (app == null) {
            throw exception(ErrorCodeConstants.APP_NOT_FOUND);
        }
        // 校验是否禁用
        if (CommonStatusEnum.isDisable(app.getStatus())) {
            throw exception(ErrorCodeConstants.APP_IS_DISABLE);
        }
        return app;
    }
}
