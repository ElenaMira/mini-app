package cn.iocoder.boot.module.promotion.service.coupon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.member.api.user.MemberUserApi;
import cn.iocoder.boot.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponPageReqVO;
import cn.iocoder.boot.module.promotion.convert.coupon.CouponTemplateConvert;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponTemplateDO;
import cn.iocoder.boot.module.promotion.dal.mysql.coupon.CouponMapper;
import cn.iocoder.boot.module.promotion.enums.coupon.CouponStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.boot.common.util.collection.CollectionUtils.convertSet;

/**
 * @author xiaosheng
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Resource
    private CouponMapper couponMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private CouponTemplateService couponTemplateService;


    @Override
    public Long getUnusedCouponCount(Long loginUserId) {
        return couponMapper.selectCountByUserIdAndStatus(loginUserId, CouponStatusEnum.UNUSED.getStatus());
    }

    @Override
    public PageResult<CouponDO> getCouponPage(CouponPageReqVO pageReqVO) {
        // 获得用户编号
        if (StrUtil.isNotEmpty(pageReqVO.getNickname())) {
            List<MemberUserRespDTO> users = memberUserApi.getUserListByNickname(pageReqVO.getNickname());
            if (CollUtil.isEmpty(users)) {
                return PageResult.empty();
            }
            pageReqVO.setUserIds(convertSet(users, MemberUserRespDTO::getId));
        }
        // 分页查询
        return couponMapper.selectPage(pageReqVO);
    }

    @Override
    public Map<Long, Boolean> getUserCanCanTakeMap(Long loginUserId, List<CouponTemplateDO> templates) {
        // 1. 未登录时，都显示可以领取
        Map<Long, Boolean> userCanTakeMap = convertMap(templates, CouponTemplateDO::getId, templateId -> true);
        if (loginUserId == null) {
            return userCanTakeMap;
        }

        // 2.1 过滤出领取数量限制的
        Set<Long> templateIds = convertSet(templates, CouponTemplateDO::getId,
                template -> !couponTemplateService.isTakeLimitCountUnlimited(template.getTakeLimitCount()));
        // 2.2 检查用户领取的数量是否超过限制
        if (CollUtil.isNotEmpty(templateIds)) {
            Map<Long, Integer> couponTakeCountMap = this.getTakeCountMapByTemplateIds(templateIds, loginUserId);
            for (CouponTemplateDO template : templates) {
                Integer takeCount = couponTakeCountMap.get(template.getId());
                userCanTakeMap.put(template.getId(), takeCount == null || takeCount < template.getTakeLimitCount());
            }
        }
        return userCanTakeMap;
    }
    @Override
    public Map<Long, Integer> getTakeCountMapByTemplateIds(Collection<Long> templateIds, Long loginUserId) {
        if (CollUtil.isEmpty(templateIds)) {
            return Collections.emptyMap();
        }
        return couponMapper.selectCountByUserIdAndTemplateIdIn(loginUserId, templateIds);
    }
}
