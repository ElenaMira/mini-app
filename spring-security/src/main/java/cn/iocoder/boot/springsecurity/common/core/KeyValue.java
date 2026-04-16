package cn.iocoder.boot.springsecurity.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiaosheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K,V> implements Serializable {
    private K key;
    private V value;
}
