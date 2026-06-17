package cn.iocoder.boot.module.pay.dal.mysql;

import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayWalletMapper extends BaseMapperX<PayWalletDO> {

    default PayWalletDO selectByUserIdAndType(Long loginUserId, Integer userType) {
        return selectOne(new LambdaQueryWrapper<PayWalletDO>()
                .eq(PayWalletDO::getUserId,loginUserId)
                .eq(PayWalletDO::getUserType,userType));
    }
}
