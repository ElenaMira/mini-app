package cn.iocoder.boot.module.promotion.dal.mysql.point;

import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointProductDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author xiaosheng
 */
@Mapper
public interface PointProductMapper extends BaseMapperX<PointProductDO> {

    default List<PointProductDO> selectListByActivityId(Set<Long> activityIds) {
        return selectList(PointProductDO::getActivityId, activityIds);
    }
}
