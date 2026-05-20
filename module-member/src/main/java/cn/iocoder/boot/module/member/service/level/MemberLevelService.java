package cn.iocoder.boot.module.member.service.level;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.module.member.dal.dataObject.app.level.MemberLevelDO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

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

    /**
     *
     * @param status 状态
     * @return 会员列表
     */
    List<MemberLevelDO> getLevelListByStatus(@NotEmpty Integer status);

    /**
     * 获取开启状态下的会员等级列表
     * @return 会员等级列表
     */
    default List<MemberLevelDO> getEnableLevelList(){
        return getLevelListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }
}
