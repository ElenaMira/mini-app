package cn.iocoder.boot.module.pay.framework.pay.core.client.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 使用RuntimeException进行捕获异常,可以实现兜底异常捕获,而不用每个支付接口都抛异常
 * @author xiaosheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayClientException extends RuntimeException {
    public PayClientException(Throwable cause) {
        super(cause);
    }
}
