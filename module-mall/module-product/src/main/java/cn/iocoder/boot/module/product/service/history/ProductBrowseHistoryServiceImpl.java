package cn.iocoder.boot.module.product.service.history;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.boot.module.product.dal.dataObject.history.ProductBrowseHistoryDO;
import cn.iocoder.boot.module.product.dal.mysql.history.BrowseHistoryMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class ProductBrowseHistoryServiceImpl implements ProductBrowseHistoryService {
    private static final int USER_STORE_MAXIMUM = 100 ;
    @Resource
    private BrowseHistoryMapper browseHistoryMapper;

    @Override
    public void createBrowseHistory(Long loginUserId, Long spuId) {
        // 用户未登录时不记录
        if (loginUserId == null) {
            return;
        }
        // 情况一：同一个商品，只保留最新的一条记录
        ProductBrowseHistoryDO history = browseHistoryMapper.selectByUserIdAndSpuId(loginUserId, spuId);
        if (history != null) {
            browseHistoryMapper.deleteById(history);
        } else {
            // 情况二：限制每个用户的浏览记录的条数
            // 基于现实很少用户会达到记录上线
            Long total = browseHistoryMapper.selectCountByUserId(loginUserId);
            if (total  >= USER_STORE_MAXIMUM) {
                Page<ProductBrowseHistoryDO> pageResult = browseHistoryMapper.selectPageByUserIdOrderByCreateTimeAsc(loginUserId, 1, 1);
                browseHistoryMapper.deleteById(CollUtil.getFirst(pageResult.getRecords()));
            }
        }
        // 插入
        ProductBrowseHistoryDO browseHistory = new ProductBrowseHistoryDO()
                .setUserId(loginUserId)
                .setSpuId(spuId);
        browseHistoryMapper.insert(browseHistory);
    }
}
