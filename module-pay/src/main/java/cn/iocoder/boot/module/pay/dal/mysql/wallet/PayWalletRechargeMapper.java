package cn.iocoder.boot.module.pay.dal.mysql.wallet;

import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayWalletRechargeMapper extends BaseMapperX<PayWalletRechargeDO> {
    default PageResult<PayWalletRechargeDO> selectPage(PageParam pageReqVO, Long walletId, Boolean payStatus) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<PayWalletRechargeDO>()
                .eq(PayWalletRechargeDO::getWalletId, walletId)
                .eq(PayWalletRechargeDO::getPayStatus, payStatus)
                .orderByDesc(PayWalletRechargeDO::getId));
    }
}
