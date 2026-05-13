package cn.iocoder.boot.module.member.service.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.member.dal.dataObject.MemberUserDO;
import cn.iocoder.boot.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.boot.module.member.mq.producer.user.MemberUserProducer;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author xiaosheng
 */
@Service
public class MemberUserServiceImpl implements MemberUserService {
    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private  MemberUserProducer memberUserProducer;

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
    @Transactional(rollbackFor = Exception.class)
    public MemberUserDO createUserIfAbsent(String mobile,String registerIp,Integer terminal) {
        MemberUserDO memberUserDO = memberUserMapper.selectByMobile(mobile);
        if (null != memberUserDO){
            return memberUserDO;
        }
        // 创建用户
        return createUser(mobile, null, null, registerIp, terminal);
    }

    @Override
    public MemberUserDO createUser(String nickname, String avatar, String registerIp, Integer terminal) {
        return createUser(null,nickname,avatar,registerIp,terminal);
    }

    private MemberUserDO createUser(String mobile, String nickname, String avtar, String registerIp, Integer terminal){
        //生成密码
        String password = IdUtil.fastUUID();
        MemberUserDO userDO =  new MemberUserDO();
        userDO.setMobile(mobile);
        userDO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        userDO.setPassword(encodePassword(password));
        userDO.setRegisterIp(registerIp).setRegisterTerminal(terminal);
        userDO.setNickname(nickname).setAvatar(avtar); // 基础信息
        if (StrUtil.isEmpty(nickname)) {
            // 昵称为空时，随机一个名字，避免一些依赖 nickname 的逻辑报错，或者有点丑。例如说，短信发送有昵称时~
            userDO.setNickname("用户" + RandomUtil.randomNumbers(6));
        }
        memberUserMapper.insert(userDO);

        // 发送 MQ 消息：用户创建
        // 解耦: 让其他逻辑异步执行,这里只管创建用户
        // 异步: 注册不用等短信发送完,不用等优惠券发放完
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            @Override
            public void afterCommit(){
                memberUserProducer.sendUserCreateMessage(userDO.getId());
            }
        });
        return userDO;
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
