package cn.iocoder.boot.module.product.service.comment;

import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.module.product.controller.app.comment.vo.AppCommentPageReqVO;
import cn.iocoder.boot.module.product.dal.dataObject.comment.ProductCommentDO;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class ProductCommentServiceImpl implements ProductCommentService {
    @Override
    public PageResult<ProductCommentDO> getCommentPage(AppCommentPageReqVO pageVO, Boolean visible) {
        return null;
    }
}
