package cn.iocoder.boot.module.promotion.convert.diy;

import cn.iocoder.boot.module.promotion.controller.app.diy.vo.AppDiyTemplatePropertyRespVO;
import cn.iocoder.boot.module.promotion.dal.dataObject.diy.DiyTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaosheng
 */
@Mapper
public interface DiyTemplateConvert {
    DiyTemplateConvert INSTANCE = Mappers.getMapper(DiyTemplateConvert.class);

    AppDiyTemplatePropertyRespVO convertPropertyVo2(DiyTemplateDO diyTemplate, String home, String user);
}
