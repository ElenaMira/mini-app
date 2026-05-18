package cn.iocoder.boot.module.member.dal.dataObject.app.user;

import cn.iocoder.boot.common.enums.TerminalEnum;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaosheng
 */
@TableName(value = "member_user",autoResultMap = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    // ========== 其它信息 ==========
    /**
     * 会员经验
     */
    private Integer experience;
    /**
     * 会员级别编号
     *
     * 关联 {@link MemberLevelDO#getId()} 字段
     */
    private Long levelId;
}
