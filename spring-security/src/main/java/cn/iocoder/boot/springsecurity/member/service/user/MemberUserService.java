package cn.iocoder.boot.springsecurity.member.service.user;

import cn.iocoder.boot.springsecurity.common.enums.TerminalEnum;
import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.springsecurity.member.vilidation.Mobile;

/**
 * @author xiaosheng
 */
public interface MemberUserService {
    MemberUserDO getMemberUser(String mobile);

    Boolean isPasswordMatch(String rawPassword, String encodePassword);

    /**
     * 基于手机号获取用户信息
     * @param mobile 手机号
     * @return
     */
    MemberUserDO getUserByMobile(@Mobile String mobile);

    /**
     * 基于用户主键Id获取用户信息
     * @param id
     * @return
     */
    MemberUserDO getUser(Long id);

    /**
     * 基于手机获取DO,如果没有则创建
     * @param mobile
     * @return
     */
    MemberUserDO createUserIfAbsent(@Mobile String mobile,String registerIp,Integer terminal);

    /**
     * 创建用户
     * 目的：三方登录时，如果未绑定用户时，自动创建对应用户
     *
     * @param nickname   昵称
     * @param avatar      头像
     * @param registerIp 注册 IP
     * @param terminal   终端 {@link TerminalEnum}
     * @return 用户对象
     */
    MemberUserDO createUser(String nickname, String avatar, String registerIp, Integer terminal);
}
