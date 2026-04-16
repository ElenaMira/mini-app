package cn.iocoder.boot.springsecurity.mybatis.mapper.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author xiaosheng
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {
    public QueryWrapperX<T> limitN(int n){
        //不用区分数据库: 交给Mybatis-plus自行翻译成对应的SQL
        super.last("LIMIT " + n);
        return this;
    }

    public QueryWrapperX<T> eqIfPresent(String column,Object val){
        if(val !=null){
            return (QueryWrapperX<T>)super.eq(column,val);
        }
        return this;
    }
    @Override
    public QueryWrapperX<T> orderByDesc(String column){
        super.orderByDesc(true,column);
        return this;
    }
    @Override
    public QueryWrapperX<T> eq(String column,Object val){
        super.eq(column,val);
        return this;
    }

}
