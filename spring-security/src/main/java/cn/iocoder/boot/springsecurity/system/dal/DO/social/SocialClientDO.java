package cn.iocoder.boot.springsecurity.system.dal.DO.social;

import cn.iocoder.boot.springsecurity.common.enums.CommonStatusEnum;
import cn.iocoder.boot.springsecurity.common.enums.SocialTypeEnum;
import cn.iocoder.boot.springsecurity.common.enums.UserTypeEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaosheng
 */
@Data
@TableName(value = "system_social_client",autoResultMap = true)
public class SocialClientDO {

    /**
     * 编号，自增
     */
    @TableId
    private Long id;
    /**
     * 应用名
     */
    private String name;
    /**
     * 社交类型
     *
     * 枚举 {@link SocialTypeEnum}
     */
    private Integer socialType;
    /**
     * 用户类型
     *
     * 目的：不同用户类型，对应不同的小程序，需要自己的配置
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 客户端 id
     */
    private String clientId;
    /**
     * 客户端 Secret
     */
    private String clientSecret;

//    /**
//     * 代理编号
//     *
//     * 目前只有部分“社交类型”在使用：
//     * 1. 企业微信：对应授权方的网页应用 ID
//     */
//    private String agentId;

//    /**
//     * publicKey 公钥
//     *
//     * 目前只有部分“社交类型”在使用：
//     * 1. 支付宝：支付宝公钥
//     */
//    private String publicKey;
}
