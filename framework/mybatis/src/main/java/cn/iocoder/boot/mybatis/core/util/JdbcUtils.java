package cn.iocoder.boot.mybatis.core.util;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @author xiaosheng
 */
public class JdbcUtils {
    /**
     * 获得 URL 对应的 DB 类型
     *
     * @param url URL
     * @return DB 类型
     */
    public static DbType getDbType(String url) {
        return com.baomidou.mybatisplus.extension.toolkit.JdbcUtils.getDbType(url);
    }
}
