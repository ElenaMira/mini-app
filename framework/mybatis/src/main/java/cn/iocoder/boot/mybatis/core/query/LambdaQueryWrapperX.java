package cn.iocoder.boot.mybatis.core.query;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;

/**
 * @author xiaosheng
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

    // ========== 重写父类方法，方便链式调用返回LambdaQueryWrapperX<T> ==========
    @Override
    public  LambdaQueryWrapperX<T> eq(boolean condition, SFunction<T, ?> column, Object val){
        super.eq(condition, column, val);
        return this;
    }
    @Override
    public LambdaQueryWrapperX<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T,?> column,Object val){
        if (ObjectUtil.isNotEmpty(val)){
            return (LambdaQueryWrapperX<T>) super.eq(column, val);
        }
        return this;
    }
    @Override
    public LambdaQueryWrapperX<T> in(SFunction<T,?> column, Collection<?> cal){
        super.in(column, cal);
        return this;
    }
}
