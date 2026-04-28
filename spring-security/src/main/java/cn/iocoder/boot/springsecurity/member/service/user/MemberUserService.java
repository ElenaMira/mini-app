package cn.iocoder.boot.springsecurity.member.service.user;

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
    MemberUserDO createUserIfAbsent(String mobile);
}
