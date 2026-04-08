package cn.iocoder.boot.springsecurity.config;



import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import lombok.Data;

import java.util.Collections;
import java.util.List;


@ConfigurationProperties("spring.security")
@Validated
@Data
public class SecurityProperties {
    /**
     *
     */
    @NotEmpty(message = "Token Header不能为空")
    private String tokenHeader = "zss";
    /**
     * 扫描等需要持久化的通信时,不能自定义Header,此时需要url带参token
     */
    @NotEmpty(message = "Token Parameter不能为空")
    private String tokenParameter = "token";
    /**
     * mock模式开关,true为开
     */
    @NotNull(message = "mock开关不能为空")
    private Boolean mockEnable = false;

    /**
     * mock密钥,用于获取模拟用户的信息
     */
    @NotEmpty(message = "mock密钥不为空")
    private String mockSecret = "test";

    /**
     * url免验证配置列表
     */
    private List<String> permitAllUrls = Collections.emptyList();
    /**
     * 用户加密复杂度
     */
    private Integer passwordEncoderLength = 4;
}
