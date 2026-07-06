package cn.iocoder.boot.module.member.api.user;

import cn.iocoder.boot.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.boot.module.member.convert.MemberUserConvert;
import cn.iocoder.boot.module.member.service.user.MemberUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaosheng
 */
@Service
public class MemberUserApiImpl implements MemberUserApi {
    @Resource
    private MemberUserService memberUserService;

    @Override
    public List<MemberUserRespDTO> getUserListByNickname(String nickname) {
        return MemberUserConvert.INSTANCE.convertList2(memberUserService.getUserListByNickname(nickname));
    }
}
