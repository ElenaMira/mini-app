package cn.iocoder.boot.springsecurity.common.pojo;

import lombok.Data;

/**
 * @author xiaosheng
 */
@Data
public class CommonResult<T> {
    private Integer code;

    private String msg;

    private T data;

    public static <T> CommonResult<T> success(T data){
        CommonResult<T> result = new CommonResult<>();
        result.setCode(200);
        result.setMsg("请求成功");
        result.setData(data);
        return result;
    }
}
