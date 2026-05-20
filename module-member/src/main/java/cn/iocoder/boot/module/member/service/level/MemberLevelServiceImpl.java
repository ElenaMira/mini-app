package cn.iocoder.boot.module.member.service.level;

import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import cn.iocoder.boot.module.member.dal.mysql.level.MemberLevelMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaosheng
 */
@Service
public class MemberLevelServiceImpl implements MemberLevelService {
    @Resource
    private MemberLevelMapper memberLevelMapper;

    @Override
    public MemberLevelDO getLevel(Long id) {
        return id != null && id > 0 ? memberLevelMapper.selectById(id) : null;
    }

    @Override
    public List<MemberLevelDO> getLevelListByStatus(Integer status) {
        return memberLevelMapper.selectListByStatus(status);
    }
}
