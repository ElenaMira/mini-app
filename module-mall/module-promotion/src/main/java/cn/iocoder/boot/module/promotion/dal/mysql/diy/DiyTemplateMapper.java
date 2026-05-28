package cn.iocoder.boot.module.promotion.dal.mysql.diy;

import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyTemplateDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface DiyTemplateMapper extends BaseMapperX<DiyTemplateDO> {
    default DiyTemplateDO selectByUsed(boolean used) {
        return selectOne(DiyTemplateDO::getUsed, used);
    }
}
