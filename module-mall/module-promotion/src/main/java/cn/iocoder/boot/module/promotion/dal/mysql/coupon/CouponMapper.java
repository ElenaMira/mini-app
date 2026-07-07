package cn.iocoder.boot.module.promotion.dal.mysql.coupon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.toolkit.MPJWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertMap;

/**
 * @author xiaosheng
 */
@Mapper
public interface CouponMapper extends BaseMapperX<CouponDO> {
    default Long selectCountByUserIdAndStatus(Long loginUserId, Integer status) {
        return selectCount(new LambdaQueryWrapper<CouponDO>()
                .eq(CouponDO::getUserId, loginUserId)
                .eq(CouponDO::getStatus, status));
    }
    default PageResult<CouponDO> selectPage(CouponPageReqVO reqVO) {
        return selectPage(reqVO,new LambdaQueryWrapperX<CouponDO>()
                .eqIfPresent(CouponDO::getTemplateId, reqVO.getTemplateId())
                .eqIfPresent(CouponDO::getStatus, reqVO.getStatus())
                .inIfPresent(CouponDO::getUserId, reqVO.getUserIds())
                .betweenIfPresent(CouponDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CouponDO::getId));
    }

    default Map<Long, Integer> selectCountByUserIdAndTemplateIdIn(Long loginUserId, Collection<Long> templateIds) {
        String templateIdAlias = "templateId";
        String countAlias = "count";
        List<Map<String, Object>> list = selectMaps(MPJWrappers.lambdaJoin(CouponDO.class)
                .selectAs(CouponDO::getTemplateId, templateIdAlias)
                .selectCount(CouponDO::getId, countAlias)
                .eq(CouponDO::getUserId, loginUserId)
                .in(CouponDO::getTemplateId, templateIds)
                .groupBy(CouponDO::getTemplateId));
        return convertMap(list,map-> MapUtil.getLong(map,templateIdAlias),map -> MapUtil.getInt(map, countAlias));
    }
}
