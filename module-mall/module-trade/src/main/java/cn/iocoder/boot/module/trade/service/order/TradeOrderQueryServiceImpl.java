package cn.iocoder.boot.module.trade.service.order;

import cn.iocoder.boot.module.trade.dal.mysql.order.TradeOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class TradeOrderQueryServiceImpl implements TradeOrderQueryService {
    @Resource
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public Long getOrderCount(Long loginUserId, Integer status, Boolean commonStatus) {
        return tradeOrderMapper.selectByUserIdAndStatus(loginUserId,status);
    }
}
