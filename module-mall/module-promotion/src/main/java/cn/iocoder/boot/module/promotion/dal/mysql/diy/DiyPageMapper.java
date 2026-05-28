package cn.iocoder.boot.module.promotion.dal.mysql.diy;

import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyPageDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaosheng
 */
@Mapper
public interface DiyPageMapper extends BaseMapperX<DiyPageDO> {
    default List<DiyPageDO> selectListByTemplateId(Long id) {
        return selectList(DiyPageDO::getTemplateId,id);
    }
}
