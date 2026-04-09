package cn.iocoder.boot.springsecurity.member.service.authService;

import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.springsecurity.member.dal.mysql.user.MemberUserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author xiaosheng
 */
public class MemberUserServiceImpl implements MemberUserService{
    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public MemberUserDO getMemberUser(String mobile) {
        return memberUserMapper.selectByMobile(mobile);
    }

    @Override
    public Boolean isPasswordMatch(String rawPassword, String encodePassword) {
        return passwordEncoder.matches(rawPassword,encodePassword);
    }
}
