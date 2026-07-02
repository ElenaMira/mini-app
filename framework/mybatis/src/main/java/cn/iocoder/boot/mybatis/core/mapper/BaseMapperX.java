package cn.iocoder.boot.mybatis.core.mapper;


import cn.hutool.core.collection.CollUtil;
import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.common.pojo.SortingField;
import cn.iocoder.boot.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
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
    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> field0, Object value0,SFunction<T, ?> field1, Object value1) {
        return selectList(new LambdaQueryWrapper<T>().eq(field0,value0).eq(field1,value1));
    }
    //========分页查询=========
    default PageResult<T> selectPage(PageParam pageParam, @Param("ew")Wrapper<T> queryWrapper){
        return selectPage(pageParam,null,queryWrapper);
    }
    default PageResult<T> selectPage(PageParam pageParam, Collection<SortingField> sortingFields, @Param("ew") Wrapper<T> queryWrapper) {
        // 特殊：不分页，直接查询全部
        if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
            MyBatisUtils.addOrder(queryWrapper,sortingFields);
            List<T> list = selectList(queryWrapper);
            return new PageResult<>(list, (long) list.size());
        }

        // MyBatis Plus 查询
        IPage<T> mpPage = MyBatisUtils.buildPage(pageParam, sortingFields);
        selectPage(mpPage, queryWrapper);
        // 转换返回
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }


}
