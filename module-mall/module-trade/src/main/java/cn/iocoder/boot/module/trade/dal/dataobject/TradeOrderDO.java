package cn.iocoder.boot.module.trade.dal.dataobject;

import cn.iocoder.boot.common.enums.TerminalEnum;
import cn.iocoder.boot.module.trade.enums.order.TradeOrderCancelTypeEnum;
import cn.iocoder.boot.module.trade.enums.order.TradeOrderStatusEnum;
import cn.iocoder.boot.module.trade.enums.order.TradeOrderTypeEnum;
import cn.iocoder.boot.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
@TableName(value = "trade_order", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeOrderDO extends BaseDO {
    // ========== 订单基本信息 ==========
    /**
     * 订单编号，主键自增
     */
    private Long id;
    /**
     * 订单流水号
     *
     * 例如说，1146347329394184195
     */
    private String no;
    /**
     * 订单类型
     *
     * 枚举 {@link TradeOrderTypeEnum}
     */
    private Integer type;
    /**
     * 订单来源
     *
     * 枚举 {@link TerminalEnum}
     */
    private Integer terminal;
    /**
     * 订单状态
     *
     * 枚举 {@link TradeOrderStatusEnum}
     */
    private Integer status;
    /**
     * 购买的商品数量
     */
    private Integer productCount;
    /**
     * 订单完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;
    /**
     * 取消类型
     *
     * 枚举 {@link TradeOrderCancelTypeEnum}
     */
    private Integer cancelType;
    /**
     * 商家备注
     */
    private String remark;
    // ========== 用户基本信息 ==========
    /**
     * 用户编号
     *
     * 关联 MemberUserDO 的 id 编号
     */
    private Long userId;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 用户备注
     */
    private String userRemark;
    /**
     * 是否评价
     *
     * true - 已评价
     * false - 未评价
     */
    private Boolean commentStatus;
    /**
     * 推广人编号
     *
     * 关联 {@link BrokerageUserDO#getId()} 字段，即 {@link MemberUserRespDTO#getId()} 字段
     */
    private Long brokerageUserId;

    // ========== 价格 + 支付基本信息 ==========
    /**
     * 支付订单编号
     *
     * PayOrderDO 的 id 编号
     */
    private Long payOrderId;
    /**
     * 是否已支付
     *
     * true - 已经支付过
     * false - 没有支付过
     */
    private Boolean payStatus;
    /**
     * 付款时间
     */
    private LocalDateTime payTime;
    /**
     * 支付渠道
     *
     * 对应 PayChannelEnum 枚举
     */
    private String payChannelCode;
    /**
     * 商品原价，单位：分
     *
     * totalPrice = {@link TradeOrderItemDO#getPrice()} * {@link TradeOrderItemDO#getCount()} 求和
     *
     * 对应 taobao 的 trade.total_fee 字段
     */
    private Integer totalPrice;
    /**
     * 优惠金额，单位：分
     *
     * 对应 taobao 的 order.discount_fee 字段
     */
    private Integer discountPrice;
    /**
     * 订单调价，单位：分
     *
     * 正数，加价；负数，减价
     */
    private Integer adjustPrice;
    /**
     * 应付金额（总），单位：分
     *
     * = {@link #totalPrice}
     * - {@link #couponPrice}
     * - {@link #pointPrice}
     * - {@link #discountPrice}
     * + {@link #deliveryPrice}
     * + {@link #adjustPrice}
     * - {@link #vipPrice}
     */
    private Integer payPrice;

    // ========== 售后基本信息 ==========
    /**
     * 售后状态
     *
     * 枚举 {@link TradeOrderRefundStatusEnum}
     */
    private Integer refundStatus;
    /**
     * 退款金额，单位：分
     *
     * 注意，退款并不会影响 {@link #payPrice} 实际支付金额
     * 也就说，一个订单最终产生多少金额的收入 = payPrice - refundPrice
     */
    private Integer refundPrice;
}
