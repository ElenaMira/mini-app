package cn.iocoder.boot.mybatis.core.mapper;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.List;


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
    /**
     * 根据俩个字段查询一条记录
     */
    default T selectOne(SFunction<T, ?> field0, Object value0,SFunction<T, ?> field1, Object value1) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field0,value0).eq(field1,value1));
    }
    /**
     * 根据三个字段查询一条记录
     */
    default T selectOne(SFunction<T, ?> field0, Object value0
            ,SFunction<T, ?> field1, Object value1
            ,SFunction<T, ?> field2, Object value3
    ) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field0,value0)
                .eq(field1,value1)
                .eq(field2,value3));
    }
    default  T selectFirst(SFunction<T, ?> field0, Object value0,SFunction<T, ?> field1, Object value1){
        List<T> list = selectList(new LambdaQueryWrapper<T>().eq(field0,value0).eq(field1,value1));
        return CollUtil.getFirst(list);
    }


    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field,value));
    }
    default List<T> selectList(SFunction<T, ?> field0, Object value0,SFunction<T, ?> field1, Object value1) {
        return selectList(new LambdaQueryWrapper<T>().eq(field0,value0).eq(field1,value1));
    }
}
