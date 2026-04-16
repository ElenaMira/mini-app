package cn.iocoder.boot.springsecurity.system.dal.mysql.sms;

import cn.iocoder.boot.springsecurity.mybatis.mapper.BaseMapperX;
import cn.iocoder.boot.springsecurity.system.dal.DO.sms.SmsTemplateDO;
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
