package cn.iocoder.boot.module.pay.dal.mysql.order;

import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayOrderMapper extends BaseMapperX<PayOrderDO> {

    default PayOrderDO selectByNo(String no) {
        return selectOne(PayOrderDO::getNo, no);
    }

    default int updateByIdAndStatus(Long id, Integer status, PayOrderDO update) {
        return update(update,new LambdaQueryWrapperX<PayOrderDO>()
                .eq(PayOrderDO::getNo, id).eq(PayOrderDO::getStatus, status));
    }
}
