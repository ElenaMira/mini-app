package cn.iocoder.boot.common.uitl.collection;

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
}
