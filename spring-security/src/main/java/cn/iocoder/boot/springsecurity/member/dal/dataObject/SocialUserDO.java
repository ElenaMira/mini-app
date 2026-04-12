package cn.iocoder.boot.springsecurity.member.dal.dataObject;

import cn.iocoder.boot.springsecurity.common.enums.SocialTypeEnum;
import cn.iocoder.boot.springsecurity.system.dal.DO.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Data
@TableName(value = "system_social_user",autoResultMap = true)
public class SocialUserDO extends BaseDO {
    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 社交平台的类型
     *
     * 枚举 {@link SocialTypeEnum}
     */
    private Integer type;

    /**
     * 社交 openid
     */
    private String openid;
    /**
     * 社交 token
     */
    private String token;
    /**
     * 原始 Token 数据，一般是 JSON 格式
     */
    private String rawTokenInfo;

    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 原始用户数据，一般是 JSON 格式
     */
    private String rawUserInfo;

    /**
     * 最后一次的认证 code
     */
    private String code;
    /**
     * 最后一次的认证 state
     */
    private String state;
}
