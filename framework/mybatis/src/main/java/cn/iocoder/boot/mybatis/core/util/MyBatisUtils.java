package cn.iocoder.boot.mybatis.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.iocoder.boot.common.pojo.PageParam;
import cn.iocoder.boot.common.pojo.SortingField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collection;

/**
 * @author xiaosheng
 */
public class MyBatisUtils {

    public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields){
        //页码+页数
        Page<T> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        page.setOptimizeJoinOfCountSql(false);
        //排序字段
        if (CollUtil.isNotEmpty(sortingFields)) {
            for (SortingField sortingField : sortingFields) {
                page.addOrder(new OrderItem()
                        .setAsc(SortingField.ORDER_ASC.equals(sortingField.getOrder()))
                        .setColumn(StrUtil.toUnderlineCase(sortingField.getField())));
            }
        }
        return page;
    }

    /**
     *
     * @param wrapper   查询器接口
     * @param sortingFields 排序字段
     * @param <T>
     */
    public static <T> void addOrder(Wrapper<T> wrapper, Collection<SortingField> sortingFields) {
        if (CollUtil.isEmpty(sortingFields)) {
            return;
        }
        if (wrapper instanceof QueryWrapper) {
            QueryWrapper<T> query = (QueryWrapper<T>) wrapper;
            for (SortingField sortingField : sortingFields) {
                query.orderBy(true,
                        SortingField.ORDER_ASC.equals(sortingField.getOrder()),
                        StrUtil.toUnderlineCase(sortingField.getField()));
            }
        }
        else if (wrapper instanceof LambdaQueryWrapper) {
            // LambdaQueryWrapper 不直接支持字符串字段排序，使用 last 方法拼接 ORDER BY
            LambdaQueryWrapper<T> lambdaQuery = (LambdaQueryWrapper<T>) wrapper;
            StringBuilder orderBy = new StringBuilder();
            for (SortingField sortingField : sortingFields) {
                if (StrUtil.isEmpty(orderBy)){
                    orderBy.append(",");
                }
                orderBy.append(StrUtil.toUnderlineCase(sortingField.getField()))
                        .append(" ")
                        .append(SortingField.ORDER_ASC.equals(sortingField.getOrder()) ? "ASC" : "DESC");
            }
            lambdaQuery.last("ORDER BY " + orderBy);
        } else {
            throw new IllegalArgumentException("Unsupported wrapper type: " + wrapper.getClass().getName());
        }
    }
}
