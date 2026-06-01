package cn.iocoder.boot.common.util.object;

import java.util.Arrays;

/**
 * @author xiaosheng
 */
public class ObjectUtils {
    /**
     *
     * @param obj 对比源对象
     * @param array 对比集合
     * @return
     * @param <T>
     */
    public static<T> Boolean equalsAny(T obj,T... array){
        return Arrays.asList(array).contains(obj);
    }
}
