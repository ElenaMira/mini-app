package cn.iocoder.boot.common.util.collection;

import cn.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author xiaosheng
 */
public class CollectionUtils {
    public static<T,U> List<U> convertList(Collection<T> collection, Function<T,U> function){
       if (collection.isEmpty()){
           return new ArrayList<>();
       }
       return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 基于func转换器和stream流转换,获取目标类下的源类的Set集合
     * @param collection 源类集合
     * @param func  转换器
     * @return
     * @param <T> 源类
     * @param <U> 目标类
     */
    public  static<T,U> Set<U> convertSet(Collection<T> collection,Function<T,U> func){
        if (CollUtil.isEmpty(collection)){
            return new HashSet<>();
        }
        return collection.stream().map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static <T,U> U findFirst(Collection<T> source, Predicate<T> predicate, Function<T,U> func){
        if (CollUtil.isEmpty(source)){
            return null;
        }
        return source.stream().filter(predicate).findFirst().map(func).orElse(null);
    }

    /**
     * 基于
     * @param source    集合源
     * @param keyFunc   规则字段
     * @return  Map
     */
    public static <T, K> Map<K,List<T>> convertMultiMap(Collection<T> source, Function<T,K> keyFunc){
        if (CollUtil.isEmpty(source)) {
            return new HashMap<>();
        }
        //显示下游处理为原来对象
        return source.stream().collect(Collectors.groupingBy(keyFunc,Collectors.mapping(t -> t, Collectors.toList())));
    }

    /**
     * 获取最小的的List
     * @param source    源list
     * @param func  排序字段或比较器
     * @return  小到大排序的List
     */
    public static <T, V extends Comparable<? super V>> T getMinObject(List<T> source,Function<T,V> func){
        if (CollUtil.isEmpty(source)){
            return null;
        }
        assert !source.isEmpty(); // 断言，避免告警
        return source.stream().min(Comparator.comparing(func)).orElse(null);
    }
}
