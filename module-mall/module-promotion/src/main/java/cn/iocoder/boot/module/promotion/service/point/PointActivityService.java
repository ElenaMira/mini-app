package cn.iocoder.boot.module.promotion.service.point;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.point.vo.PointActivityPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointActivityDO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointProductDO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * @author xiaosheng
 */
public interface PointActivityService {
    /**
     * 获取可用的积分单
     * @param reqVO
     * @return
     */
    PageResult<PointActivityDO> getPointActivityPage(@Valid PointActivityPageReqVO reqVO);

    /**
     * 基于activityIds获取可用的积分商品
     * @param activityIds
     * @return
     */
    List<PointProductDO> getPointProductListByActivityIds(Set<Long> activityIds);
}
