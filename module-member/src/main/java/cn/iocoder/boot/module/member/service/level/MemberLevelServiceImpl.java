package cn.iocoder.boot.module.member.service.level;

import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import cn.iocoder.boot.module.member.dal.mysql.level.MemberLevelMapper;
import jakarta.annotation.Resource;

/**
 * @author xiaosheng
 */
public class MemberLevelServiceImpl implements MemberLevelService {
    @Resource
    private MemberLevelMapper memberLevelMapper;

    @Override
    public MemberLevelDO getLevel(Long id) {
        return id != null && id > 0 ? memberLevelMapper.selectById(id) : null;
    }
}
