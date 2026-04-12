package cn.iocoder.boot.springsecurity.common.Object;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author xiaosheng
 * BeanUtil隔离层:哪怕是只有一行也需要隔离  依赖倒置 + 隔离第三方 + 统一入口
 */
public class BeanUtils {
    /**
     *
     * @param source    源类
     * @param targetClass   目标类
     * @return
     * @param <T> 基于hutool工具包实现
     */
    public static<T> T toBean(Object source ,Class<T> targetClass){
        return BeanUtil.toBean(source, targetClass);
    }
}
