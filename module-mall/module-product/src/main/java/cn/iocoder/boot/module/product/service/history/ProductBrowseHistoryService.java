package cn.iocoder.boot.module.product.service.history;

import jakarta.validation.constraints.NotNull;
import org.springframework.scheduling.annotation.Async;

/**
 * @author xiaosheng
 */
public interface ProductBrowseHistoryService {
    /**
     * 创建商品浏览记录
     * 记录限额100条
     * 采用悲观客户流量量策略.先查是否超过限额再判断是否删除
     *
     * @param loginUserId 用户编号
     * @param spuId  SPU 编号
     */
    @Async
    void createBrowseHistory(@NotNull Long loginUserId, Long spuId);
}
