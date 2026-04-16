package cn.iocoder.boot.springsecurity.mybatis.mapper.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

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
}
