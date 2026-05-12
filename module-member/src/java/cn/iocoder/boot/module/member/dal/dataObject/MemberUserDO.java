package cn.iocoder.boot.module.member.dal.dataObject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @author xiaosheng
 */
@TableName(value = "member_user",autoResultMap = true)
@Data
public class MemberUserDO {
    // ========== 账号信息 ==========
    @TableId//企业标准: 强制用包装类区分：未赋值 vs 赋值为 0
    private Long id;

    private String mobile;

    private String password;

    private Integer status;
    /**
     * 注册 IP
     */
    private String registerIp;
    /**
     * 注册终端
     * 枚举 {@link TerminalEnum}
     */
    private Integer registerTerminal;
    // ========== 基础信息 ==========
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
}
