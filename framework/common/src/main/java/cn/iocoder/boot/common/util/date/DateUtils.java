package cn.iocoder.boot.common.util.date;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
public class DateUtils {

    /**
     * 秒转换成毫秒
     */
    public static final long SECOND_MILLIS = 1000;
    /**
     *
     */
    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    /**
     *
     * @param date 需要匹配的日期
     * @return
     */
    public static boolean isToday(LocalDateTime date){
        return LocalDateTimeUtil.isSameDay(date, LocalDateTime.now());
    }

    public static   LocalDateTime addTime(Duration duration){
        return LocalDateTime.now().plus(duration);
    }

    /**
     * 过期返回true
     * @param eTime  过期时间
     * @return
     */
    public static boolean isExpired(LocalDateTime eTime){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(eTime);
    }

}
