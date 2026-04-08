package cn.iocoder.boot.springsecurity.common.Object;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author xiaosheng
 */
public class BeanUtils {
    /**
     *
     * @param source    源类
     * @param targetClass   目标类
     * @return
     * @param <T> 基于hutool工具包实现
     */
    public static<T> T getBean(Object source ,Class<T> targetClass){
        return BeanUtil.toBean(source, targetClass);
    }
}
