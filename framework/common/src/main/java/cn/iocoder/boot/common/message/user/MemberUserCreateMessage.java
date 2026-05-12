package cn.iocoder.boot.common.message.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Data
public class MemberUserCreateMessage {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;
}
