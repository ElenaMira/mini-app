package cn.iocoder.boot.module.member.service.level;

import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;

/**
 * @author xiaosheng
 */
public interface MemberLevelService {
    /**
     *  获得会员等级
     * @param id    用户Id
     * @return      会员等级
     */
    MemberLevelDO getLevel(Long id);
}
