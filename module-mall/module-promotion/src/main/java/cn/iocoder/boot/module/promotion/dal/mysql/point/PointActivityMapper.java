package cn.iocoder.boot.module.promotion.dal.mysql.point;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.point.vo.PointActivityPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointActivityDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PointActivityMapper extends BaseMapperX<PointActivityDO> {
    default PageResult<PointActivityDO> selectPage(PointActivityPageReqVO reqVO) {
        return selectPage(reqVO,new LambdaQueryWrapperX<PointActivityDO>()
                .eqIfPresent(PointActivityDO::getStatus, reqVO.getStatus())
                .orderByDesc(PointActivityDO::getSort));
    }
}
