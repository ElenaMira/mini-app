package cn.iocoder.boot.springsecurity.common.uitl.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author xiaosheng
 */
public class HttpUtils {
    @SneakyThrows
    public static String encodeUtf8(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    }
    /**
     * HTTP post 请求，基于 {@link cn.hutool.http.HttpUtil} 实现
     *
     * 为什么要封装该方法，因为 HttpUtil 默认封装的方法，没有允许传递 headers 参数
     *
     * @param url URL
     * @param headers 请求头
     * @param requestBody 请求体
     * @return 请求结果
     */
    public static String post(String url, Map<String, String> headers, String requestBody) {
        try (HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .body(requestBody)
                .execute()) {
            return response.body();
        }
    }
}
