package cn.iocoder.boot.module.product.service.comment;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.product.controller.app.comment.vo.AppCommentPageReqVO;
import cn.iocoder.boot.module.product.dal.dataObject.comment.ProductCommentDO;
import jakarta.validation.Valid;

/**
 * @author xiaosheng
 */
public interface ProductCommentService {
    /**
     *  【会员】获得商品评价分页
     * @param pageVO    分页查询
     * @param visible   是否可见
     * @return  商品评价分页
     */
    PageResult<ProductCommentDO> getCommentPage(@Valid AppCommentPageReqVO pageVO, Boolean visible);
}
