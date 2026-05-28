package cn.iocoder.boot.common.Object;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.boot.common.uitl.collection.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
