package cn.iocoder.boot.module.member.service.user;


import cn.iocoder.boot.common.enums.TerminalEnum;
import cn.iocoder.boot.common.validation.Mobile;
import cn.iocoder.boot.module.member.controller.app.social.vo.AppMemberUserUpdateMobileByWeixinReqVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdateMobileReqVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdatePasswordReqVO;
import cn.iocoder.boot.module.member.controller.app.user.vo.AppMemberUserUpdateReqVO;
import cn.iocoder.boot.module.member.dal.dataObject.app.user.MemberUserDO;
import jakarta.validation.Valid;


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

    void updateUser(Long loginUserId, @Valid AppMemberUserUpdateReqVO reqVO);

    void updateUserPassword(Long loginUserId, @Valid AppMemberUserUpdatePasswordReqVO reqVO);

    void updateUserMobileByWeixin(Long loginUserId, @Valid AppMemberUserUpdateMobileByWeixinReqVO reqVO);

    /**
     * 基于线程用户,验证码,手机号更新手机号
     * @param loginUserId   线程用户Id
     * @param reqVO vo
     */
    void updateUserMobile(Long loginUserId, @Valid AppMemberUserUpdateMobileReqVO reqVO);
}
