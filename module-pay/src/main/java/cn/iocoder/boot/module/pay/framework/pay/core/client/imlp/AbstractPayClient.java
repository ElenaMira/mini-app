package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp;

import cn.iocoder.boot.common.exception.ServiceException;
import cn.iocoder.boot.common.util.validation.ValidationUtils;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderRespDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.dto.pay.PayOrderUnifiedReqDTO;
import cn.iocoder.boot.module.pay.framework.pay.core.client.exception.PayClientException;
import lombok.extern.slf4j.Slf4j;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClient;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientConfig;

import static cn.iocoder.boot.common.util.json.JsonUtils.toJsonString;


/**
 *
 *
 */
@Slf4j
public abstract class AbstractPayClient<Config extends PayClientConfig> implements PayClient<Config> {
    /**
     * 渠道编号
     */
    private final Long channelId;
    /**
     * 渠道编码
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final String channelCode;
    /**
     * 支付配置
     */
    protected Config config;

    public AbstractPayClient(Long channelId, String channelCode, Config config) {
        this.channelId = channelId;
        this.channelCode = channelCode;
        this.config = config;
    }

    public void init(){
        doInit();
        log.debug("[init][客户端({}) 初始化完成]", getId());
    }
    /**
     * 自定义初始化
     */
    protected abstract void doInit();
    @Override
    public Long getId() {
        return channelId;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    /**
     * 刷新
     * @param config    对应的配置类
     */
    public final void refresh(Config config) {
        // 判断是否更新
        if (config.equals(this.config)) {
            return;
        }
        log.info("[refresh][客户端({})发生变化，重新初始化]", getId());
        this.config = config;
        // 初始化
        this.init();
    }

    @Override
    public final PayOrderRespDTO getOrder(String outTradeNo) {
        try{
            return doGetOrder(outTradeNo);
        }catch (ServiceException ex){
            throw ex;
        }catch (Throwable e){
            log.error("[getOrder][客户端({}) outTradeNo({}) 查询支付单异常]"
                    ,getId(),outTradeNo,e);
            throw buildPayException(e);
        }
    }
    @Override
    public final PayOrderRespDTO unifiedOrder(PayOrderUnifiedReqDTO reqDTO) {
        ValidationUtils.validate(reqDTO);
        // 执行统一下单
        PayOrderRespDTO resp;
        try {
            resp = doUnifiedOrder(reqDTO);
        } catch (ServiceException ex) { // 业务异常，都是实现类已经翻译，所以直接抛出即可
            throw ex;
        } catch (Throwable ex) {
            // 系统异常，则包装成 PayException 异常抛出
            log.error("[unifiedOrder][客户端({}) request({}) 发起支付异常]",
                    getId(), toJsonString(reqDTO), ex);
            throw buildPayException(ex);
        }
        return resp;
    }

    protected abstract PayOrderRespDTO doUnifiedOrder(PayOrderUnifiedReqDTO reqDTO)
            throws Throwable;


    /**
     *
     * @param e 异常
     * @return  包装成独立的支付异常
     */
    private PayClientException buildPayException(Throwable e) {
        if (e instanceof PayClientException) {
            return (PayClientException) e;
        }
        throw new PayClientException(e);
    }

    protected abstract PayOrderRespDTO doGetOrder(String outTradeNo) throws Throwable;

}
