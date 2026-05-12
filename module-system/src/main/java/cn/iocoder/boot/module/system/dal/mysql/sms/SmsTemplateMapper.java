package cn.iocoder.boot.module.system.dal.mysql.sms;

import cn.iocoder.boot.module.system.dal.DO.sms.SmsTemplateDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface SmsTemplateMapper extends BaseMapperX<SmsTemplateDO> {
    default SmsTemplateDO selectByCode(String code){
        return selectOne(SmsTemplateDO::getCode,code);
    }
}
