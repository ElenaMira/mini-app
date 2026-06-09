package cn.iocoder.boot.module.member.service.user;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import cn.iocoder.boot.common.Object.BeanUtils;
import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.common.enums.UserTypeEnum;
import cn.iocoder.boot.module.member.controller.app.social.vo.AppMemberUserUpdateMobileByWeixinReqVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdatePasswordReqVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdateReqVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.user.MemberUserDO;
import cn.iocoder.boot.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.boot.module.member.mq.producer.user.MemberUserProducer;
import cn.iocoder.boot.module.system.api.sms.SmsCodeApi;
import cn.iocoder.boot.module.system.api.sms.dto.SmsCodeUseReqDTO;
import cn.iocoder.boot.module.system.api.social.SocialClientApi;
import cn.iocoder.boot.module.system.api.social.dto.SocialWxPhoneNumberInfoRespDTO;
import cn.iocoder.boot.module.system.enums.sms.SmsSceneEnum;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static cn.iocoder.boot.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.boot.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.boot.module.member.enums.ErrorCodeConstants.USER_MOBILE_USED;
import static cn.iocoder.boot.module.member.enums.ErrorCodeConstants.USER_NOT_EXISTS;

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

    @Resource
    private SmsCodeApi smsCodeApi;

    @Resource
    private SocialClientApi socialClientApi;

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
    @Transactional(rollbackFor = Exception.class)
    public MemberUserDO createUser(String nickname, String avatar, String registerIp, Integer terminal) {
        return createUser(null,nickname,avatar,registerIp,terminal);
    }

    @Override
    public void updateUser(Long loginUserId, AppMemberUserUpdateReqVO reqVO) {
        MemberUserDO memberUserDO = BeanUtils.toBean(reqVO, MemberUserDO.class).setId(loginUserId);
        memberUserMapper.updateById(memberUserDO);
    }

    @Override
    public void updateUserPassword(Long loginUserId, AppMemberUserUpdatePasswordReqVO reqVO) {
        // 校验用户是否存在
        MemberUserDO memberUserDO = validateUserExists(loginUserId);
        //校验验证码
        smsCodeApi.useSmsCode(SmsCodeUseReqDTO.builder()
                        .mobile(memberUserDO.getMobile())
                        .scene(SmsSceneEnum.MEMBER_UPDATE_PASSWORD.getScene())
                        .code(reqVO.getCode())
                        .usedIp(getClientIP())
                .build());
        //更新密码
        memberUserMapper.updateById(MemberUserDO.builder()
                        .id(loginUserId)
                        .password(passwordEncoder.encode(reqVO.getPassword()))
                .build());
    }

    @Override
    public void updateUserMobileByWeixin(Long loginUserId, AppMemberUserUpdateMobileByWeixinReqVO reqVO) {
        //基于Code获取对应手机号信息
        SocialWxPhoneNumberInfoRespDTO phoneNumberInfo = socialClientApi.getWxMaPhoneNumberInfo(
                UserTypeEnum.MEMBER.getValue(), reqVO.getCode());
        Assert.notNull(phoneNumberInfo, "获得手机信息失败，结果为空");
        // 1.2 校验新手机是否已经被绑定
        validateMobileUnique(loginUserId, phoneNumberInfo.getPhoneNumber());
        // 2. 更新用户手机
        memberUserMapper.updateById(MemberUserDO.builder().id(loginUserId).mobile(phoneNumberInfo.getPhoneNumber()).build());
    }

    private MemberUserDO validateUserExists(Long loginUserId) {
        if (loginUserId == null){
            return null;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(loginUserId);
        if (memberUserDO == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return memberUserDO;
    }

    /**
     *  校验手机号是否绑定
     * @param loginUserId  登录用户的ID
     * @param mobile    手机号
     */
    @VisibleForTesting
    void validateMobileUnique(Long loginUserId, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        MemberUserDO user = memberUserMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 loginUserId 为空，说明不用比较是否为相同 loginUserId 的用户
        if (loginUserId == null) {
            throw exception(USER_MOBILE_USED, mobile);
        }
        if (!user.getId().equals(loginUserId)) {
            throw exception(USER_MOBILE_USED, mobile);
        }
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
