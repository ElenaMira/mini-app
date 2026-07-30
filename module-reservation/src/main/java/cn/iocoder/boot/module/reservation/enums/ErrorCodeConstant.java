package cn.iocoder.boot.module.reservation.enums;

import cn.iocoder.boot.common.exception.ErrorCode;

/**
 *
 * 模块 gym 错误码区间 [1-024-000-000 ~ 1-025-000-000)
 * @author xiaosheng
 */
public interface ErrorCodeConstant {
    /**
     * 预约日期不能为过去日期
     */
    ErrorCode RESERVATION_DATE_EXPIRED = new ErrorCode(1_024_000_001, "预约日期不能为过去日期");

    /**
     * 当日未开放预约
     */
    ErrorCode RESERVATION_DATE_CLOSED = new ErrorCode(1_024_000_002, "当日暂未开放预约");

    /**
     * 所选预约时段无效/已停用
     */
    ErrorCode RESERVATION_SLOT_INVALID = new ErrorCode(1_024_000_003, "所选时段无效，请重新选择");

    /**
     * 该时段预约名额已约满
     */
    ErrorCode RESERVATION_SLOT_FULL = new ErrorCode(1_024_000_004, "该时段名额已约满");

    /**
     * 您今日已预约场次，不可重复预约
     */
    ErrorCode RESERVATION_USER_DAY_REPEAT = new ErrorCode(1_024_000_005, "您今日已预约场次，不可重复预约");

}
