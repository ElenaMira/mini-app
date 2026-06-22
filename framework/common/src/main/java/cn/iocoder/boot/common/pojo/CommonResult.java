package cn.iocoder.boot.common.pojo;

import cn.hutool.core.lang.Assert;
import cn.iocoder.boot.common.exception.ErrorCode;
import cn.iocoder.boot.common.exception.enums.GlobalErrorCodeConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Objects;

/**
 * @author xiaosheng
 */
@Data
public class CommonResult<T> {
    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer code;

    private String msg;

    private T data;

    public static <T> CommonResult<T> success(T data){
        CommonResult<T> result = new CommonResult<>();
        result.setCode(GlobalErrorCodeConstants.SUCCESS.getCode());
        result.setMsg("请求成功");
        result.setData(data);
        return result;
    }
    public static CommonResult<?> error(ErrorCode error){
        return error(error.getCode(), error.getMsg());
    }

    public static <T> CommonResult<T> error(Integer code,String message){
        Assert.notEquals(GlobalErrorCodeConstants.SUCCESS.getCode(), code, "code 必须是错误的！");
        CommonResult<T> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    public static Boolean isSuccess(Integer code){
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

    @JsonIgnore // 避免 jackson 序列化
    public Boolean isSuccess(){
        return isSuccess(code);
    }
}
