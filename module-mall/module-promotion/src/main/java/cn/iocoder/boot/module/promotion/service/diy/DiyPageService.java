package cn.iocoder.boot.module.promotion.service.diy;

import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyPageDO;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface DiyPageService {
    List<DiyPageDO> getDiyPageByTemplateId(Long id);
}
