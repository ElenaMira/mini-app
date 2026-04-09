package cn.iocoder.boot.springsecurity.system.enums;

import cn.iocoder.boot.springsecurity.common.exception.ErrorCode;

/**
 * @author xiaosheng
 */
public interface ErrorCodeConstant {
   ErrorCode SOCIAL_USER_AUTH_FAILURE =  new ErrorCode(1_002_018_000, "社交授权失败，原因是：{}");
}
