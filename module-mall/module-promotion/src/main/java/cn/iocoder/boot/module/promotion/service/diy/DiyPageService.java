package cn.iocoder.boot.module.promotion.service.diy;

import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyPageDO;

import java.util.List;

/**
 * @author xiaosheng
 */
public interface DiyPageService {
    /**
     * 基于模板ID获取Page模板
     * @param id
     * @return
     */
    List<DiyPageDO> getDiyPageByTemplateId(Long id);
}
