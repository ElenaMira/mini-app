package cn.iocoder.boot.module.promotion.service.diy;

import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyTemplateDO;
import cn.iocoder.boot.module.promotion.dal.mysql.diy.DiyTemplateMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xiaosheng
 */
@Service
public class DiyTemplateServiceImpl implements DiyTemplateService {
    @Resource
    private DiyTemplateMapper diyTemplateMapper;
    @Override
    public DiyTemplateDO getUsedDiyTemplate() {
        return diyTemplateMapper.selectByUsed(true);
    }
}
