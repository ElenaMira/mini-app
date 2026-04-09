package cn.iocoder.boot.springsecurity.system.api.oauth2Token.dto;

import lombok.Data;

import java.time.LocalDateTime;



/**
 * @author xiaosheng
 */
@Data
public class OAuth2AccessTokenCreateRespDTO {

    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;
}
