package cn.iocoder.boot.springsecurity.common.uitl.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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
}
