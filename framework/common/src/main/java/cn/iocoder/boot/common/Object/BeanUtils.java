package cn.iocoder.boot.common.Object;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.boot.common.pojo.PageResult;
import cn.iocoder.boot.common.util.collection.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiaosheng
 * BeanUtil隔离层:哪怕是只有一行也需要隔离  依赖倒置 + 隔离第三方 + 统一入口
 */
public class BeanUtils {
    /**
     *
     * @param source    源类
     * @param targetClass   目标类
     * @return
     * @param <T> 基于hutool工具包实现
     */
    public static<T> T toBean(Object source ,Class<T> targetClass){
        return BeanUtil.toBean(source, targetClass);
    }

    /**
     *
     * @param source
     * @param targetType
     * @return
     * @param <S>
     * @param <T>
     */
    public static <S, T> PageResult<T> toBean(PageResult<S> source, Class<T> targetType) {
        return toBean(source, targetType, null);
    }

    /**
     *  将PageResult<S>转为<T>
     * @param source
     * @param targetType
     * @param peek
     * @return
     * @param <S>
     * @param <T>
     */
    public static <S, T> PageResult<T> toBean(PageResult<S> source, Class<T> targetType, Consumer<T> peek) {
        if (source == null) {
            return null;
        }
        List<T> list = toBean(source.getList(), targetType);
        if (peek != null) {
            list.forEach(peek);
        }
        return new PageResult<>(list, source.getTotal());
    }


    /**
     *
     * @param source    源类
     * @param targetClass   目标类
     * @return
     * @param <T> 基于hutool工具包实现
     */
    public static<S,T> List<T> toBean(List<S> source , Class<T> targetClass){
        if (source == null){
            return null;
        }
        return CollectionUtils.convertList(source,s->toBean(s, targetClass));
    }
}
