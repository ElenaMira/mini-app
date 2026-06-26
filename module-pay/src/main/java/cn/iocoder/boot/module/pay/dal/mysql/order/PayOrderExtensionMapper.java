package cn.iocoder.boot.module.pay.dal.mysql.order;

import cn.iocoder.boot.module.pay.dal.dataobject.order.PayOrderExtensionDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayOrderExtensionMapper extends BaseMapperX<PayOrderExtensionDO> {
    default List<PayOrderExtensionDO> selectListByOrderIdAndStatus(Long id, Integer status) {
        return selectList(PayOrderExtensionDO::getOrderId,id
                ,PayOrderExtensionDO::getStatus,status);
    }

    default PayOrderExtensionDO selectByNo(String outTradeNo) {
        return selectOne(PayOrderExtensionDO::getOrderId,outTradeNo);
    }

    default int updateByIdAndStatus(Long id, Integer status, PayOrderExtensionDO update) {
        return update(update,new LambdaQueryWrapper<PayOrderExtensionDO>()
                .eq(PayOrderExtensionDO::getOrderId,id).eq(PayOrderExtensionDO::getStatus,status));
    }

    default PayOrderExtensionDO selectByOrderId(Long id) {
        return selectOne(PayOrderExtensionDO::getOrderId,id);
    }

    default List<PayOrderExtensionDO> selectListByOrderId(Long id) {
        return selectList(PayOrderExtensionDO::getOrderId,id);
    }
}
