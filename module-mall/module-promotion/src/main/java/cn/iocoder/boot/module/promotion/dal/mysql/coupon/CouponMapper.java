package cn.iocoder.boot.module.promotion.dal.mysql.coupon;

import cn.iocoder.boot.module.promotion.dal.dataObject.coupon.CouponDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
