package cn.iocoder.boot.module.pay.dal.mysql.channel;

import cn.iocoder.boot.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.boot.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaosheng
 */
@Mapper
public interface PayChannelMapper extends BaseMapperX<PayChannelDO> {
    default PayChannelDO selectByAppIdAndCode(Long appId, String code) {
        return selectOne(new LambdaQueryWrapper<PayChannelDO>()
                .eq(PayChannelDO::getAppId, appId)
                .eq(PayChannelDO::getCode, code));
    }
}
