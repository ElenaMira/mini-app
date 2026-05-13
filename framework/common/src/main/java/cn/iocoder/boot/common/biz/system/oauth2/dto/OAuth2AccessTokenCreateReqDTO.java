package cn.iocoder.boot.common.biz.system.oauth2.dto;

import cn.iocoder.boot.common.enums.UserTypeEnum;
import cn.iocoder.boot.common.validation.InEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaosheng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccessTokenCreateReqDTO implements Serializable {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    @InEnum(value = UserTypeEnum.class, message = "用户类型必须是 {value}")
    private Integer userType;
    /**
     * 客户端编号
     */
    @NotNull(message = "客户端编号不能为空")
    private String clientId;
    /**
     * 授权范围
     */
    private List<String> scopes;
}
