package cn.iocoder.boot.springsecurity.common.uitl.date;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
public class DateUtils {
    /**
     *
     * @param date 需要匹配的日期
     * @return
     */
    public static boolean isToday(LocalDateTime date){
        return LocalDateTimeUtil.isSameDay(date, LocalDateTime.now());
    }
}
