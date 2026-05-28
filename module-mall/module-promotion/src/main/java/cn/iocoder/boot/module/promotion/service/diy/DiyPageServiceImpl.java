package cn.iocoder.boot.module.promotion.service.diy;

import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyPageDO;
import cn.iocoder.boot.module.promotion.dal.mysql.diy.DiyPageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaosheng
 */
@Service
public class DiyPageServiceImpl implements DiyPageService {
    @Resource
    private DiyPageMapper diyPageMapper;
    @Override
    public List<DiyPageDO> getDiyPageByTemplateId(Long id) {
        return diyPageMapper.selectListByTemplateId(id);
    }
}
