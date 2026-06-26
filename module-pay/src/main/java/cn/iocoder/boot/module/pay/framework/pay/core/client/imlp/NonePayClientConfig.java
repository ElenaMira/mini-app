package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp;

import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientConfig;
import jakarta.validation.Validator;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Data
public class NonePayClientConfig implements PayClientConfig {
    /**
     * 配置名称
     * <p>
     * 如果不加任何属性，JsonUtils.parseObject2 解析会报错，所以暂时加个名称
     */
    private String name;

    public NonePayClientConfig(){
        this.name = "none-config";
    }

    @Override
    public void validate(Validator validator) {
        //无需校验
    }
}
