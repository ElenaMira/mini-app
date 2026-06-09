package org.example.module.pay.framework.pay.core.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Validator;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true) // 目的：忽略未知的属性，避免反序列化失败
public interface PayClientConfig {
    /**
     * 参数校验
     *
     * @param validator 校验对象
     */
    void validate(Validator validator);
}
