package cn.iocoder.boot.springsecurity.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author xiaosheng
 */
@Data
@AllArgsConstructor
public class ErrorCode {
    /**
     *  错误码
     */
    private Integer code;
    /**
     *  错误信息
     */
    private String msg;
}
