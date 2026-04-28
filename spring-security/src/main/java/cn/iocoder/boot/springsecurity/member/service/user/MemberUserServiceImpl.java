package cn.iocoder.boot.springsecurity.member.service.user;

import cn.iocoder.boot.springsecurity.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.springsecurity.member.dal.mysql.user.MemberUserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class MemberUserServiceImpl implements MemberUserService {
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

    @Override
    public MemberUserDO getUserByMobile(String mobile) {
        return memberUserMapper.selectByMobile(mobile);
    }

    @Override
    public MemberUserDO getUser(Long id) {
        return memberUserMapper.selectById(id);
    }

    @Override
    public MemberUserDO createUserIfAbsent(String mobile) {
        MemberUserDO memberUserDO = memberUserMapper.selectByMobile(mobile);
        if (null == memberUserDO){

        }

    }
}
