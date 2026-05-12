package cn.iocoder.boot.module.system.dal.mysql.sms;


import cn.iocoder.boot.module.system.dal.DO.sms.SmsCodeDO;

import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.QueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface SmsCodeMapper extends BaseMapperX<SmsCodeDO> {
    default SmsCodeDO selectByLastMobile(String mobile,Integer scene,String code){
        return selectOne(new QueryWrapperX<SmsCodeDO>()
                .eq("mobile",mobile)
                .eqIfPresent("scene",scene)
                .eqIfPresent("code",code)
                .orderByDesc("id")
                .limitN(1)
        );
    }
}
