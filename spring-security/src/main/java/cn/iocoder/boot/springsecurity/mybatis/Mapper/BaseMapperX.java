package cn.iocoder.boot.springsecurity.mybatis.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.databind.ser.Serializers;


/**
 * @author xiaosheng
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

    /**
     * 根据单个字段查询一条记录
     */
    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field,value));
    }
}
