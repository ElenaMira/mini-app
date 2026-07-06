package cn.iocoder.boot.common.util.string;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.xkcoding.http.util.StringUtil;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaosheng
 */
public class StrUtils {
    /**
     * 给定字符串是否以任何一个字符串开始
     * 给定字符串和数组为空都返回 false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @since 3.0.6
     */
    public static boolean startWithAny(String str, Collection<String> prefixes){
        if(StringUtil.isEmpty(str)|| ArrayUtil.isEmpty(prefixes)){
            return false;
        }
        for(CharSequence prefix:prefixes){
            if(StrUtil.startWith(str,prefix,false)){
                return true;
            }
        }
        return false;
    }

    /**
     * 将str按分割符转化为List<Long>
     * @param str
     * @param separator
     * @return
     */
    public static List<Long> splitToLong(String str,CharSequence separator){
        long[] longs = StrUtil.splitToLong(str, separator);
        //泛型只能存对象,所以这里使用boxed
        return Arrays.stream(longs).boxed().collect(Collectors.toList());
    }

}
