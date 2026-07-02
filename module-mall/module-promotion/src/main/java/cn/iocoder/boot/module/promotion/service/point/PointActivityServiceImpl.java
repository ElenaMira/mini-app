package cn.iocoder.boot.module.promotion.service.point;

import cn.iocoder.boot.common.enums.CommonStatusEnum;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.point.vo.PointActivityPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointActivityDO;
import cn.iocoder.boot.module.promotion.dal.dataObject.point.PointProductDO;
import cn.iocoder.boot.module.promotion.dal.mysql.point.PointActivityMapper;
import cn.iocoder.boot.module.promotion.dal.mysql.point.PointProductMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

/**
 * @author xiaosheng
 */
@Service
@Validated
public class PointActivityServiceImpl implements PointActivityService {
    @Resource
    private PointActivityMapper pointActivityMapper;
    @Resource
    private PointProductMapper pointProductMapper;
    @Override
    public PageResult<PointActivityDO> getPointActivityPage(PointActivityPageReqVO reqVO) {
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        return pointActivityMapper.selectPage(reqVO);
    }

    @Override
    public List<PointProductDO> getPointProductListByActivityIds(Set<Long> activityIds) {
        return pointProductMapper.selectListByActivityId(activityIds);
    }
}
