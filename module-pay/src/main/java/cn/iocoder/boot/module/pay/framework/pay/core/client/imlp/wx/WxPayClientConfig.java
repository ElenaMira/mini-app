package cn.iocoder.boot.module.pay.framework.pay.core.client.imlp.wx;


import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import cn.iocoder.boot.module.pay.framework.pay.core.client.PayClientConfig;
import lombok.Data;

/**
 * 功能描述:
 *
 * @author xiaosinian
 */
@Data
public class WxPayClientConfig implements PayClientConfig {


    /**
     * API 版本 - V2(预留)
     *
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_1">V2 协议说明</a>
     */
//    public static final String API_VERSION_V2 = "v2";


    /**
     * API 版本 - V3
     *
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay-1.shtml">V3 协议说明</a>
     */
    public static final String API_VERSION_V3 = "v3";


    /**
     * 公众号或者小程序的 appid
     *
     * 只有公众号或小程序需要该字段
     */
    @NotBlank(message = "APPID 不能为空", groups = {V3.class})
    private String appId;
    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空", groups = {V3.class})
    private String mchId;
    /**
     * API 版本
     */
    @NotBlank(message = "API 版本不能为空", groups = {V3.class})
    private String apiVersion;


    // ========== V3 版本的参数 ==========
    /**
     * apiclient_key.pem 证书文件的对应字符串
     */
    @NotBlank(message = "apiclient_key 不能为空", groups = V3.class)
    private String privateKeyContent;
    /**
     * apiV3 密钥值
     */
    @NotBlank(message = "apiV3 密钥值不能为空", groups = V3.class)
    private String apiV3Key;
    /**
     * 证书序列号（merchantSerialNumber）
     */
    @NotBlank(message = "证书序列号不能为空", groups = V3.class)
    private String certSerialNo;

    /**
     * pub_key.pem 证书文件的对应字符串
     */
    private String publicKeyContent;
    @NotBlank(message = "publicKeyId 不能为空", groups = V3.class)
    private String publicKeyId;

    /**
     * 分组校验 v3版本
     */
    public interface V3 {
    }

    @Override
    public void validate(Validator validator) {
//        ValidationUtils.validate(validator, this,
//                 V3.class);
    }
}
