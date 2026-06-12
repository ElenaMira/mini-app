package cn.iocoder.boot.common.exception.enums;

import cn.iocoder.boot.common.exception.ErrorCode;

/**
 * @author xiaosheng
 * 接口默认静态常量(static final)
 *
 *
 */
public interface GlobalErrorCodeConstants {
    /**
     * todo: yudao项目的历史遗留问题,前端不好改以后再说 
     */
    ErrorCode SUCCESS = new ErrorCode(0,"成功");

    // ========== 客户端错误段 ==========
    ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数不正确");
    ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");

    // ========== 服务端错误段 ==========

    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常");
    ErrorCode NOT_IMPLEMENTED = new ErrorCode(501, "功能未实现/未开启");
    ErrorCode ERROR_CONFIGURATION = new ErrorCode(502, "错误的配置项");

}
