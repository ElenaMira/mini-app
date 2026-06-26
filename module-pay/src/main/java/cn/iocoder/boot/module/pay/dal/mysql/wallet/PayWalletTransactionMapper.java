package cn.iocoder.boot.module.pay.dal.mysql.wallet;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.boot.mybatis.core.query.QueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionPageReqVO.TYPE_EXPENSE;
import static cn.iocoder.boot.module.pay.controller.app.wallet.vo.AppPayWalletTransactionPageReqVO.TYPE_INCOME;
import static cn.iocoder.boot.module.pay.enums.ErrorCodeConstants.WALLET_TRANSACTION_TYPE_NOT_EXISTS;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayWalletTransactionMapper extends BaseMapperX<PayWalletTransactionDO> {
    default PageResult<PayWalletTransactionDO> selectPage(Long walletId, Integer type,
                                                          PageParam pageParam, LocalDateTime[] createTime) {
        LambdaQueryWrapperX<PayWalletTransactionDO> query = new LambdaQueryWrapperX<PayWalletTransactionDO>()
                .eq(PayWalletTransactionDO::getWalletId, walletId);
        if (ObjectUtil.equal(type,TYPE_INCOME)){
            query.gt(PayWalletTransactionDO::getPrice, 0);
        }else if (Objects.equals(type, TYPE_EXPENSE)) {
            query.lt(PayWalletTransactionDO::getPrice, 0);
        }else{
            throw new ServiceException(WALLET_TRANSACTION_TYPE_NOT_EXISTS);
        }
        query.betweenIfPresent(PayWalletTransactionDO::getCreateTime, createTime);
        query.orderByDesc(PayWalletTransactionDO::getId);
        return selectPage(pageParam, query);
    }

    //todo 优化和金查询
    default Integer selectPriceSum(Long walletId, Integer type, LocalDateTime[] createTime) {
        // SQL sum 查询
        List<Map<String, Object>> result = selectMaps(new QueryWrapperX<PayWalletTransactionDO>()
                .select("SUM(price) AS priceSum")
                .gt(Objects.equals(type, TYPE_INCOME), "price", 0) // 收入
                .lt(Objects.equals(type, TYPE_EXPENSE), "price", 0) // 支出
                .eq("wallet_id", walletId)
                .between("create_time", createTime[0], createTime[1]));
        // 获得 sum 结果
        Map<String, Object> first = CollUtil.getFirst(result);
        return MapUtil.getInt(first, "priceSum", 0);
    }
}
