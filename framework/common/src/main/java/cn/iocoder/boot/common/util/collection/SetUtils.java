package cn.iocoder.boot.common.util.collection;

import cn.hutool.core.collection.CollUtil;

import java.util.Set;

/**
 * @author xiaosheng
 */
public class SetUtils {
    @SafeVarargs //取消堆污染警告
    public static <T> Set<T> asSet(T... objs) {
        return CollUtil.newHashSet(objs);
    }
}
