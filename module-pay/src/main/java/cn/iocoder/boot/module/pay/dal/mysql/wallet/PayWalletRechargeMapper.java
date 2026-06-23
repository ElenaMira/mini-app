package cn.iocoder.boot.module.pay.dal.mysql.wallet;

import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayWalletRechargeMapper extends BaseMapperX<PayWalletRechargeDO> {
}
