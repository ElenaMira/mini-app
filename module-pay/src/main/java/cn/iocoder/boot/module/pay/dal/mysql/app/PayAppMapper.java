package cn.iocoder.boot.module.pay.dal.mysql.app;

import cn.iocoder.boot.module.pay.dal.dataobject.app.PayAppDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.boot.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayAppMapper extends BaseMapperX<PayAppDO> {
    default PayAppDO selectByAppKey(String appKey) {
        return selectOne(PayAppDO::getAppKey, appKey);
    }
}
