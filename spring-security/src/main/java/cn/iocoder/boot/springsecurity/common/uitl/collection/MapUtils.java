package cn.iocoder.boot.springsecurity.common.uitl.collection;

import cn.iocoder.boot.springsecurity.common.core.KeyValue;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author xiaosheng
 */
public class MapUtils {
    public static<K,V> Map<K,V> convertMap(List<KeyValue<K, V>> keyValues){
        Map<K,V> map = Maps.newLinkedHashMapWithExpectedSize(keyValues.size());
        keyValues.forEach(keyValue->map.put(keyValue.getKey(),keyValue.getValue()));
        return map;
    }
}
