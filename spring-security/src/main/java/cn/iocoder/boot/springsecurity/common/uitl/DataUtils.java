package cn.iocoder.boot.springsecurity.common.uitl;

import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
public class DataUtils {
    public static boolean isExpired(LocalDateTime time){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(time);
    }
}
