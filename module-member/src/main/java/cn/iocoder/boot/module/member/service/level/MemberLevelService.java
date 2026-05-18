package cn.iocoder.boot.module.member.service.level;

import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import jakarta.annotation.Nullable;

/**
 * @author xiaosheng
 */
public interface MemberLevelService {
    /**
     *  获得会员等级
     * @param id    用户关联的LevelId
     * @return      会员等级
     */
    MemberLevelDO getLevel(@Nullable Long id);
}
