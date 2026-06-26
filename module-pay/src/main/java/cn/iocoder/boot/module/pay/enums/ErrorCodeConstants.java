package cn.iocoder.boot.module.pay.enums;

import cn.iocoder.boot.common.exception.ErrorCode;

/**
 * @author xiaosheng
 */
public interface ErrorCodeConstants {
    // ========== APP 模块 1-007-000-000 ==========
    ErrorCode APP_NOT_FOUND = new ErrorCode(1_007_000_000, "App 不存在");
    ErrorCode APP_IS_DISABLE = new ErrorCode(1_007_000_002, "App 已经被禁用");
    ErrorCode APP_EXIST_ORDER_CANT_DELETE =  new ErrorCode(1_007_000_003, "支付应用存在支付订单，无法删除");
    ErrorCode APP_EXIST_REFUND_CANT_DELETE =  new ErrorCode(1_007_000_004, "支付应用存在退款订单，无法删除");
    ErrorCode APP_KEY_EXISTS = new ErrorCode(1_007_000_005, "支付应用标识已经存在");

    // ========== CHANNEL 模块 1-007-001-000 ==========
    ErrorCode CHANNEL_NOT_FOUND = new ErrorCode(1_007_001_000, "支付渠道的配置不存在");
    ErrorCode CHANNEL_IS_DISABLE = new ErrorCode(1_007_001_001, "支付渠道已经禁用");
    ErrorCode CHANNEL_EXIST_SAME_CHANNEL_ERROR = new ErrorCode(1_007_001_004, "已存在相同的渠道");

    // ========== ORDER 模块 1-007-002-000 ==========
    ErrorCode PAY_ORDER_NOT_FOUND = new ErrorCode(1_007_002_000, "支付订单不存在");
    ErrorCode PAY_ORDER_STATUS_IS_NOT_WAITING = new ErrorCode(1_007_002_001, "支付订单不处于待支付");
    ErrorCode PAY_ORDER_STATUS_IS_SUCCESS = new ErrorCode(1_007_002_002, "订单已支付，请刷新页面");
    ErrorCode PAY_ORDER_IS_EXPIRED = new ErrorCode(1_007_002_003, "支付订单已经过期");
    ErrorCode PAY_ORDER_SUBMIT_CHANNEL_ERROR = new ErrorCode(1_007_002_004, "发起支付报错，错误码：{}，错误提示：{}");
    ErrorCode PAY_ORDER_REFUND_FAIL_STATUS_ERROR = new ErrorCode(1_007_002_005, "支付订单退款失败，原因：状态不是已支付或已退款");


    // ========== ORDER 模块(拓展单) 1-007-003-000 ==========
    ErrorCode PAY_ORDER_EXTENSION_NOT_FOUND = new ErrorCode(1_007_003_000, "支付交易拓展单不存在");
    ErrorCode PAY_ORDER_EXTENSION_STATUS_IS_NOT_WAITING = new ErrorCode(1_007_003_001, "支付交易拓展单不处于待支付");
    ErrorCode PAY_ORDER_EXTENSION_IS_PAID = new ErrorCode(1_007_003_002, "订单已支付，请等待支付结果");

    // ========== 钱包充值模块 1-007-008-000 ==========
    ErrorCode WALLET_RECHARGE_NOT_FOUND = new ErrorCode(1_007_008_000, "钱包充值记录不存在");
    ErrorCode WALLET_RECHARGE_UPDATE_PAID_STATUS_NOT_UNPAID = new ErrorCode(1_007_008_001, "钱包充值更新支付状态失败，钱包充值记录不是【未支付】状态");
    ErrorCode WALLET_RECHARGE_UPDATE_PAID_PAY_ORDER_ID_ERROR = new ErrorCode(1_007_008_002, "钱包充值更新支付状态失败，支付单编号不匹配");
    ErrorCode WALLET_RECHARGE_UPDATE_PAID_PAY_ORDER_STATUS_NOT_SUCCESS = new ErrorCode(1_007_008_003, "钱包充值更新支付状态失败，支付单状态不是【支付成功】状态");
    ErrorCode WALLET_RECHARGE_UPDATE_PAID_PAY_PRICE_NOT_MATCH = new ErrorCode(1_007_008_004, "钱包充值更新支付状态失败，支付单金额不匹配");
    ErrorCode WALLET_RECHARGE_REFUND_FAIL_NOT_PAID = new ErrorCode(1_007_008_005, "钱包发起退款失败，钱包充值订单未支付");
    ErrorCode WALLET_RECHARGE_REFUND_FAIL_REFUNDED = new ErrorCode(1_007_008_006, "钱包发起退款失败，钱包充值订单已退款");
    ErrorCode WALLET_RECHARGE_REFUND_BALANCE_NOT_ENOUGH = new ErrorCode(1_007_008_007, "钱包发起退款失败，钱包余额不足");
    ErrorCode WALLET_RECHARGE_REFUND_FAIL_REFUND_ORDER_ID_ERROR = new ErrorCode(1_007_008_008, "钱包退款更新失败，钱包退款单编号不匹配");
    ErrorCode WALLET_RECHARGE_REFUND_FAIL_REFUND_NOT_FOUND = new ErrorCode(1_007_008_009, "钱包退款更新失败，退款订单不存在");
    ErrorCode WALLET_RECHARGE_REFUND_FAIL_REFUND_PRICE_NOT_MATCH = new ErrorCode(1_007_008_010, "钱包退款更新失败，退款单金额不匹配");
    ErrorCode WALLET_RECHARGE_PACKAGE_NOT_FOUND = new ErrorCode(1_007_008_011, "钱包充值套餐不存在");
    ErrorCode WALLET_RECHARGE_PACKAGE_IS_DISABLE = new ErrorCode(1_007_008_012, "钱包充值套餐已禁用");
    ErrorCode WALLET_RECHARGE_PACKAGE_NAME_EXISTS = new ErrorCode(1_007_008_013, "钱包充值套餐名称已存在");

    // ========== 钱包余额明细模块 1-007-008-100 ==========
    ErrorCode WALLET_TRANSACTION_TYPE_NOT_EXISTS = new ErrorCode(1_007_008_100, "钱包余额类型不存在");

}
