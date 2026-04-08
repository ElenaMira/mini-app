package cn.iocoder.boot.springsecurity.common.exception.enums;

import cn.iocoder.boot.springsecurity.common.exception.ErrorCode;

/**
 * @author xiaosheng
 * 接口默认静态常量(static final)
 */
public interface GlobalErrorCodeConstants {

    ErrorCode SUCCESS = new ErrorCode(0,"成功");

    ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");
}
