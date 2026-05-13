package cn.iocoder.boot.common.uitl.json;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.boot.common.uitl.json.databind.TimestampLocalDateTimeDeserializer;
import cn.iocoder.boot.common.uitl.json.databind.TimestampLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author xiaosheng
 */
@Slf4j
public class JsonUtils {
    @Getter
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 忽略 null 值
        // 解决 LocalDateTime 的序列化
        SimpleModule simpleModule = new JavaTimeModule()
                .addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
                .addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
        objectMapper.registerModules(simpleModule);
    }

    /**
     * 初始化 objectMapper 属性
     * <p>
     * 通过这样的方式，使用 Spring 创建的 ObjectMapper Bean
     *
     * @param objectMapper ObjectMapper 对象
     */
    public static void init(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    @SneakyThrows
    public static String toJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, clazz);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

}
