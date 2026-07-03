package cn.iocoder.boot.common.util.collection;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.boot.common.core.KeyValue;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author xiaosheng
 */
public class MapUtils {
    /**
     *
     * @param keyValues
     * @return
     * @param <K>
     * @param <V>
     */
    public static<K,V> Map<K,V> convertMap(List<KeyValue<K, V>> keyValues){
        Map<K,V> map = Maps.newLinkedHashMapWithExpectedSize(keyValues.size());
        keyValues.forEach(keyValue->map.put(keyValue.getKey(),keyValue.getValue()));
        return map;
    }

    /**
     * 基于Jdk的消费者处理Map的value
     * 其中key和value和map均不为null
     * @param map
     * @param key
     * @param consumer
     * @param <K>
     * @param <V>
     */
    public static<K,V> void findAndThen(Map<K,V> map, K key, Consumer<V> consumer){
        if (ObjectUtil.isNull(map)||ObjectUtil.isNull(key)){
            return;
        }
        V v = map.get(key);
        if (ObjectUtil.isNull(v)){
            return;
        }
        consumer.accept(v);
    }
}
