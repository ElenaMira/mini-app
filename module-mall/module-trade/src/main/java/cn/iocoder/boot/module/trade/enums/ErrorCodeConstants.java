package cn.iocoder.boot.module.trade.enums;

import cn.iocoder.boot.common.exception.ErrorCode;

/**
 * @author xiaosheng
 */
public interface ErrorCodeConstants {

    // ========== Order 模块 1-011-000-000 ==========
    ErrorCode ORDER_ITEM_NOT_FOUND = new ErrorCode(1_011_000_010, "交易订单项不存在");
    ErrorCode ORDER_NOT_FOUND = new ErrorCode(1_011_000_011, "交易订单不存在");
    ErrorCode ORDER_ITEM_UPDATE_AFTER_SALE_STATUS_FAIL = new ErrorCode(1_011_000_012, "交易订单项更新售后状态失败，请重试");
    ErrorCode ORDER_UPDATE_PAID_STATUS_NOT_UNPAID = new ErrorCode(1_011_000_013, "交易订单更新支付状态失败，订单不是【未支付】状态");
    ErrorCode ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR = new ErrorCode(1_011_000_014, "交易订单更新支付状态失败，支付单编号不匹配");
    ErrorCode ORDER_UPDATE_PAID_FAIL_PAY_ORDER_STATUS_NOT_SUCCESS = new ErrorCode(1_011_000_015, "交易订单更新支付状态失败，支付单状态不是【支付成功】状态");
    ErrorCode ORDER_UPDATE_PAID_FAIL_PAY_PRICE_NOT_MATCH = new ErrorCode(1_011_000_016, "交易订单更新支付状态失败，支付单金额不匹配");
    ErrorCode ORDER_DELIVERY_FAIL_STATUS_NOT_UNDELIVERED = new ErrorCode(1_011_000_017, "交易订单发货失败，订单不是【待发货】状态");
    ErrorCode ORDER_RECEIVE_FAIL_STATUS_NOT_DELIVERED = new ErrorCode(1_011_000_018, "交易订单收货失败，订单不是【待收货】状态");
    ErrorCode ORDER_COMMENT_FAIL_STATUS_NOT_COMPLETED = new ErrorCode(1_011_000_019, "创建交易订单项的评价失败，订单不是【已完成】状态");
    ErrorCode ORDER_COMMENT_STATUS_NOT_FALSE = new ErrorCode(1_011_000_020, "创建交易订单项的评价失败，订单已评价");
    ErrorCode ORDER_DELIVERY_FAIL_REFUND_STATUS_NOT_NONE = new ErrorCode(1_011_000_021, "交易订单发货失败，订单已退款或部分退款");
    ErrorCode ORDER_DELIVERY_FAIL_COMBINATION_RECORD_STATUS_NOT_SUCCESS = new ErrorCode(1_011_000_022, "交易订单发货失败，拼团未成功");
    ErrorCode ORDER_DELIVERY_FAIL_BARGAIN_RECORD_STATUS_NOT_SUCCESS = new ErrorCode(1_011_000_023, "交易订单发货失败，砍价未成功");
    ErrorCode ORDER_DELIVERY_FAIL_DELIVERY_TYPE_NOT_EXPRESS = new ErrorCode(1_011_000_024, "交易订单发货失败，发货类型不是快递");
    ErrorCode ORDER_CANCEL_FAIL_STATUS_NOT_UNPAID = new ErrorCode(1_011_000_025, "交易订单取消失败，订单不是【待支付】状态");
    ErrorCode ORDER_UPDATE_PRICE_FAIL_PAID = new ErrorCode(1_011_000_026, "支付订单调价失败，原因：支付订单已付款,不能调价");
    ErrorCode ORDER_UPDATE_PRICE_FAIL_ALREADY = new ErrorCode(1_011_000_027, "支付订单调价失败，原因：已经修改过价格");
    ErrorCode ORDER_UPDATE_PRICE_FAIL_PRICE_ERROR = new ErrorCode(1_011_000_028, "支付订单调价失败，原因：调整后支付价格不能小于 0.01 元");
    ErrorCode ORDER_DELETE_FAIL_STATUS_NOT_CANCEL = new ErrorCode(1_011_000_029, "交易订单删除失败，订单不是【已取消】状态");
    ErrorCode ORDER_RECEIVE_FAIL_DELIVERY_TYPE_NOT_PICK_UP = new ErrorCode(1_011_000_030, "交易订单自提失败，收货方式不是【用户自提】");
    ErrorCode ORDER_UPDATE_ADDRESS_FAIL_STATUS_NOT_DELIVERED = new ErrorCode(1_011_000_031, "交易订单修改收货地址失败，原因：订单不是【待发货】状态");
    ErrorCode ORDER_CREATE_FAIL_EXIST_UNPAID = new ErrorCode(1_011_000_032, "交易订单创建失败，原因：存在未付款订单");
    ErrorCode ORDER_CANCEL_PAID_FAIL = new ErrorCode(1_011_000_033, "交易订单取消支付失败，原因：订单不是【{}】状态");
    ErrorCode ORDER_UPDATE_PAID_ORDER_REFUNDED_FAIL_REFUND_NOT_FOUND = new ErrorCode(1_011_000_034, "交易订单更新支付订单退款状态失败，原因：退款单不存在");
    ErrorCode ORDER_UPDATE_PAID_ORDER_REFUNDED_FAIL_REFUND_STATUS_NOT_SUCCESS = new ErrorCode(1_011_000_035, "交易订单更新支付订单退款状态失败，原因：退款单状态不是【退款成功】");
    ErrorCode ORDER_PICK_UP_FAIL_NOT_VERIFY_USER = new ErrorCode(1_011_000_036, "交易订单自提失败，原因：你没有核销该门店订单的权限");
    ErrorCode ORDER_PICK_UP_FAIL_COMBINATION_NOT_SUCCESS = new ErrorCode(1_011_000_037, "交易订单自提失败，原因：商品拼团记录不是【成功】状态");
    ErrorCode ORDER_CREATE_FAIL_INSUFFICIENT_USER_POINTS = new ErrorCode(1_011_000_038, "交易订单创建失败，原因：用户积分不足");
    ErrorCode ORDER_PICK_UP_FAIL_STATUS_NOT_UNDELIVERED = new ErrorCode(1_011_000_039, "交易订单自提失败，订单不是【待核销】状态");

}
