package cn.iocoder.boot.springsecurity.system.api.social.dto;

import cn.iocoder.boot.springsecurity.common.enums.SocialTypeEnum;
import cn.iocoder.boot.springsecurity.common.enums.UserTypeEnum;
import cn.iocoder.boot.springsecurity.common.validation.InEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author xiaosheng
 */
public class SocialUserBindReqDTO {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    /**
     * 用户类型
     */
    @InEnum(UserTypeEnum.class)
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    /**
     * 社交平台的类型
     */
    @InEnum(SocialTypeEnum.class)
    @NotNull(message = "社交平台的类型不能为空")
    private Integer socialType;
    /**
     * 授权码
     */
    @NotEmpty(message = "授权码不能为空")
    private String code;
    /**
     * state
     */
    @NotNull(message = "state 不能为空")
    private String state;

}
