package cn.iocoder.boot.module.promotion.service.coupon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.member.api.user.MemberUserApi;
import cn.iocoder.boot.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.boot.module.promotion.controller.app.coupon.vo.CouponPageReqVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
import cn.iocoder.boot.module.promotion.dal.mysql.coupon.CouponMapper;
import cn.iocoder.boot.module.promotion.enums.coupon.CouponStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
