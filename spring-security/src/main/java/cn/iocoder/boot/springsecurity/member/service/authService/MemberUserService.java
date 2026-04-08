package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;

/**
 * @author xiaosheng
 */
public interface MemberUserService {
    MemberUserDO getMemberUser(String mobile);

    Boolean isPasswordMatch(String rawPassword, String encodePassword);
}
