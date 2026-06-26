package cn.iocoder.boot.common.util.collection;

/**
 * @author xiaosheng
 */
public class ArrayUtils {
    public static <T> T get(T[] array, int index) {
        if (null == array || index >= array.length) {
            return null;
        }
        return array[index];
    }
}
