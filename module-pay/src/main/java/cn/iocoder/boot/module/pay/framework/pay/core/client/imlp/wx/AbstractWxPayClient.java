package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.util.io.FileUtils;
import cn.iocoder.boot.common.util.object.ObjectUtils;
import cn.iocoder.boot.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderUnifiedReqDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.AbstractPayClient;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

import static cn.hutool.core.date.DatePattern.UTC_WITH_XXX_OFFSET_PATTERN;
import static cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx.WxPayClientConfig.API_VERSION_V3;

/**
 * 功能描述:
 *
 * @author xiaosinian
 */
public abstract class AbstractWxPayClient extends AbstractPayClient<WxPayClientConfig> {

    protected WxPayService client;

    public AbstractWxPayClient(Long channelId, String channelCode, WxPayClientConfig config) {
        super(channelId, channelCode, config);
    }

    /**
     * 初始化 client 客户端
     *
     * @param tradeType 交易类型
     */
    protected void doInit(String tradeType){
        //创建 config 配置
        WxPayConfig payConfig = new WxPayConfig();
        BeanUtil.copyProperties(config,payConfig,"keyContent", "privateKeyContent", "publicKeyContent");
        payConfig.setTradeType(tradeType);
        //使用JVM临时文件存储公钥和私钥
        if (Objects.equals(config.getApiVersion(), API_VERSION_V3)){
            //微信文档要求公钥ID和私钥ID二选一
            payConfig.setPrivateKeyPath(FileUtils.createTempFile(config.getPrivateKeyContent()).getPath());
            if (StrUtil.isNotEmpty(config.getPrivateKeyContent())){
                payConfig.setPublicKeyPath(FileUtils.createTempFile(config.getPublicKeyContent()).getPath());
            }
            // 特殊：强制使用微信公钥模式，避免灰度期间的问题！！！
            payConfig.setStrictlyNeedWechatPaySerial(true);
        }
        // 创建 client 客户端
        client = new WxPayServiceImpl();
        client.setConfig(payConfig);
    }

    /**
     * 【V3】调用支付渠道，统一下单
     *
     * @param reqDTO 下单信息
     * @return 各支付渠道的返回结果
     */
    protected abstract PayOrderRespDTO doUnifiedOrderV3(PayOrderUnifiedReqDTO reqDTO)
            throws WxPayException;
    /**
     * 【V3】创建微信下单请求
     *
     * @param reqDTO 下信息
     * @return 下单请求
     */
    protected WxPayUnifiedOrderV3Request buildPayUnifiedOrderRequestV3(PayOrderUnifiedReqDTO reqDTO) {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(reqDTO.getOutTradeNo());
        request.setDescription(reqDTO.getSubject());
        request.setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(reqDTO.getPrice())); // 单位分
        request.setTimeExpire(formatDateV3(reqDTO.getExpireTime()));
        request.setSceneInfo(new WxPayUnifiedOrderV3Request.SceneInfo().setPayerClientIp(reqDTO.getUserIp()));
        request.setNotifyUrl(reqDTO.getNotifyUrl());
        return request;
    }


    @Override
    protected PayOrderRespDTO doGetOrder(String outTradeNo) throws Throwable{
        try {
            switch (config.getApiVersion()) {
                case API_VERSION_V3:
                    client.getConfig().setApiV3HttpClient(null);
                    return doGetOrderV3(outTradeNo);
                default:
                    throw new IllegalArgumentException(String.format("未知的 API 版本(%s)", config.getApiVersion()));
            }
        }catch (WxPayException e){
            if (ObjectUtils.equalsAny(e.getErrCode(),"ORDERNOTEXIST", "ORDER_NOT_EXIST")){
                String errorCode = getErrorCode(e);
                String errorMessage = getErrorMessage(e);
                return PayOrderRespDTO.closedOf(errorCode,errorMessage,outTradeNo,e.getXmlString());
            }
            throw e;
        }
    }


    private PayOrderRespDTO doGetOrderV3(String outTradeNo) throws WxPayException{
        // 构建 WxPayUnifiedOrderRequest 对象
        WxPayOrderQueryV3Request Request = new WxPayOrderQueryV3Request()
                .setOutTradeNo(outTradeNo);
        //  执行请求
        WxPayOrderQueryV3Result response = client.queryOrderV3(Request);
        // 转换结果
        Integer status = praseStatus(response.getTradeState());
        String openid = response.getPayer() != null ? response.getPayer().getOpenid() : null;
        return PayOrderRespDTO.of(status, response.getTransactionId(), openid, parseDateV3(response.getSuccessTime()),
                outTradeNo, response);

    }

    private LocalDateTime parseDateV3(String successTime) {
        return LocalDateTimeUtil.parse(successTime,UTC_WITH_XXX_OFFSET_PATTERN);
    }

    /**
     * wx支付响应状态统一转化为项目枚举
     * 详细见<a href="https://pay.weixin.qq.com/doc/v3/merchant/4012791900#%E5%BA%94%E7%AD%94%E5%8F%82%E6%95%B0">...</a>
     * @param tradeState    响应交易状态
     * @return  枚举code
     */
    private static Integer praseStatus(String tradeState) {
        switch (tradeState) {
            case "NOTPAY":
            case "USERPAYING": // 支付中，等待用户输入密码（条码支付独有）
                return PayOrderStatusEnum.WAITING.getStatus();
            case "SUCCESS":
                return PayOrderStatusEnum.SUCCESS.getStatus();
            case "REFUND":
                return PayOrderStatusEnum.REFUND.getStatus();
            case "CLOSED":
            case "REVOKED": // 已撤销（刷卡支付独有）
            case "PAYERROR": // 支付失败（其它原因，如银行返回失败）
                return PayOrderStatusEnum.CLOSED.getStatus();
            default:
                throw new IllegalArgumentException(StrUtil.format("未知的支付状态({})", tradeState));
        }
    }

    private String getErrorCode(WxPayException e) {
        if (StrUtil.isNotEmpty(e.getErrCode())){
            return e.getErrCode();
        }
        if (StrUtil.isNotEmpty(e.getCustomErrorMsg())){
            return "CUSTOM_ERROR";
        }
        return e.getReturnCode();
    }
    private String getErrorMessage(WxPayException e) {
        if (StrUtil.isNotEmpty(e.getErrCode())){
            return e.getErrCode();
        }
        if (StrUtil.isNotEmpty(e.getCustomErrorMsg())){
            return e.getCustomErrorMsg();
        }
        return e.getReturnMsg();
    }

    static String formatDateV3(LocalDateTime time){
        return TemporalAccessorUtil.format(time.atZone(ZoneId.systemDefault()),UTC_WITH_XXX_OFFSET_PATTERN);
    }
}
