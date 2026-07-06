package cn.iocoder.boot.module.member.api.user;

import cn.iocoder.boot.module.member.api.user.dto.MemberUserRespDTO;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface MemberUserApi {
    /**
     *
     * @param nickname
     * @return
     */
    List<MemberUserRespDTO> getUserListByNickname(String nickname);
}
