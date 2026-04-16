package cn.iocoder.boot.springsecurity.system.framework.sms.core.client.impl;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.boot.springsecurity.common.core.KeyValue;
import cn.iocoder.boot.springsecurity.common.uitl.collection.MapUtils;
import cn.iocoder.boot.springsecurity.common.uitl.http.HttpUtils;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.iocoder.boot.springsecurity.system.framework.sms.core.property.SmsChannelProperties;
import cn.iocoder.boot.springsecurity.system.uitl.json.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaosheng
 */
@Slf4j
public class AliyunSmsClient extends AbstractSmsClient{
    private static final String URL = "https://dysmsapi.aliyuncs.com";
    private static final String HOST = "dysmsapi.aliyuncs.com";
    private static final String VERSION = "2017-05-25";

    private static final String RESPONSE_CODE_SUCCESS = "OK";

    public AliyunSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }
    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile, String apiTemplateId,
                                  List<KeyValue<String, Object>> templateParams) throws Throwable{
        Assert.notBlank(properties.getSignature(), "短信签名不能为空");

        //1. 执行请求
        //参考链接 https://api.aliyun.com/document/Dysmsapi/2017-05-25/SendSms
        TreeMap<String, Object> queryParam = new TreeMap<>();
        queryParam.put("PhoneNumbers", mobile);
        queryParam.put("SignName", properties.getSignature());
        queryParam.put("TemplateCode", apiTemplateId);
        queryParam.put("TemplateParam", JsonUtils.toJsonString(MapUtils.convertMap(templateParams)));
        queryParam.put("OutId", sendLogId);
        JSONObject response = request("SendSms", queryParam);

        log.info("请求成功,响应:{}",response);
        // 2. 解析请求
        return new SmsSendRespDTO()
                .setSuccess(Objects.equals(response.getStr("Code"), RESPONSE_CODE_SUCCESS))
                .setSerialNo(response.getStr("BizId"))
                .setApiRequestId(response.getStr("RequestId"))
                .setApiCode(response.getStr("Code"))
                .setApiMsg(response.getStr("Message"));
    }


    /**
     * 请求阿里云短信
     *
     * @see <a href="https://help.aliyun.com/zh/sdk/product-overview/v3-request-structure-and-signature">V3 版本请求体&签名机制</>
     * @param apiName 请求的 API 名称
     * @param queryParams 请求参数
     * @return 请求结果
     */
    private JSONObject request(String apiName,TreeMap<String, Object> queryParams){
        // 1. 请求参数(规范化查询字符串)
        String queryString = queryParams.entrySet().stream()
                .map(entry -> percentCode(entry.getKey()) + "=" + percentCode(String.valueOf(entry.getValue())))
                .collect(Collectors.joining("&"));
        //2. 请求 Body
        String requestBody = ""; // 短信 API 为 RPC 接口，query parameters 在 uri 中拼接，因此 request body 如果没有特殊要求，设置为空
        String hashedRequestPayload = DigestUtil.sha256Hex(requestBody);

        // 3.1 请求 Header
        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("host", HOST);
        headers.put("x-acs-version", VERSION);
        headers.put("x-acs-action", apiName);
        headers.put("x-acs-date", FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("GMT")).format(new Date()));
        headers.put("x-acs-signature-nonce", IdUtil.randomUUID());
        headers.put("x-acs-content-sha256", hashedRequestPayload);


        // 3.2 构建(规范化请求头)签名 Header
        StringBuilder canonicalHeaders = new StringBuilder(); // 构造请求头，多个规范化消息头，按照消息头名称（小写）的字符代码顺序以升序排列后拼接在一起
        StringBuilder signedHeadersBuilder = new StringBuilder(); // 已签名消息头列表，多个请求头名称（小写）按首字母升序排列并以英文分号（;）分隔
        headers.entrySet().stream().filter(entry->entry.getKey().toLowerCase().startsWith("x-acs-")
                ||"host".equalsIgnoreCase(entry.getKey())
                ||"content-type".equalsIgnoreCase(entry.getKey()))
                .sorted(Map.Entry.comparingByKey()).forEach(entry->{
                    String lowerKey = entry.getKey().toLowerCase();
                    canonicalHeaders.append(lowerKey).append(":").append(entry.getValue().trim()).append("\n");
                    signedHeadersBuilder.append(lowerKey).append(";");
                });
        //去掉最后多余的分号
        String signedHeaders = signedHeadersBuilder.substring(0, signedHeadersBuilder.length() - 1);

        // 4. 构建 Authorization 签名
        String canonicalRequest = "POST" + "\n" +
                "/" + '\n' +         // 规范化URI
                queryString + '\n' + // 规范化查询字符串
                canonicalHeaders + '\n' +     // 规范化消息头
                signedHeaders + '\n' +        // 已签名消息头
                hashedRequestPayload;		// RequestBody经过hash处理后的值

        String hashedCanonicalRequest = DigestUtil.sha256Hex(canonicalRequest);
        //步骤二：构造待签名字符串
        String stringToSign = "ACS3-HMAC-SHA256" + "\n" + hashedCanonicalRequest;
        //步骤三：计算签名
        String signature = SecureUtil.hmacSha256(properties.getApiSecret()).digestHex(stringToSign); // 计算签名
        //步骤四：将签名添加到请求中<Authorization>
        headers.put("Authorization", "ACS3-HMAC-SHA256" + " " + "Credential=" + properties.getApiKey()
                + ", " + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature);
        // 5. 发起请求(treeMap -> Key:Value)
        String responseBody = HttpUtils.post(URL + "?" +queryString,headers, requestBody);
        return JSONUtil.parseObj(responseBody);

    }


    /**
     * 对指定的字符串进行 URL 编码，并对特定的字符进行替换，以符合URL编码规范
     * <a href="https://help.aliyun.com/zh/sdk/product-overview/v3-request-structure-and-signature">V3 版本请求体&签名机制</>
     *
     * @param str 需要进行 URL 编码的字符串
     * @return 编码后的字符串
     */
    @SneakyThrows
    private static String percentCode(String str) {
        Assert.notNull(str, "str 不能为空");
        return HttpUtils.encodeUtf8(str)
                .replace("+", "%20") // 加号 "+" 被替换为 "%20"
                .replace("*", "%2A") // 星号 "*" 被替换为 "%2A"
                .replace("%7E", "~"); // 波浪号 "%7E" 被替换为 "~"
    }
}
