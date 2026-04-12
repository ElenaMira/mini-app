package cn.iocoder.boot.springsecurity.system.dal.DO;

import cn.iocoder.boot.springsecurity.common.enums.UserTypeEnum;
import cn.iocoder.boot.springsecurity.member.dal.dataObject.SocialUserDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName(value = "system_social_user_bind", autoResultMap = true)
@Data
@Builder
public class SocialUserBindDO extends BaseDO{

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 关联的用户编号
     *
     * 关联 UserDO 的编号
     */
    private Long userId;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;

    /**
     * 社交平台的用户编号
     *
     * 关联 {@link SocialUserDO#getId()}
     */
    private Long socialUserId;
    /**
     * 社交平台的类型
     *
     * 冗余 {@link SocialUserDO#getType()}
     */
    private Integer socialType;

}
