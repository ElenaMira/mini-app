package cn.iocoder.boot.module.trade.dal.mysql;

import cn.iocoder.boot.module.trade.dal.dataobject.TradeOrderDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface TradeOrderMapper extends BaseMapperX<TradeOrderDO> {
    default Long selectByUserIdAndStatus(Long loginUserId, Integer status) {
        return selectCount(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, loginUserId));
    }
}
