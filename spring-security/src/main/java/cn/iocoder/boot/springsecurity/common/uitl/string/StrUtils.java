package cn.iocoder.boot.springsecurity.common.uitl.string;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.xkcoding.http.util.StringUtil;

import java.util.Collection;

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

}
