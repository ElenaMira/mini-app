package cn.iocoder.boot.common.util.json;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.boot.common.util.json.databind.TimestampLocalDateTimeDeserializer;
import cn.iocoder.boot.common.util.json.databind.TimestampLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
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

    /**
     *  基于JackSon将源内容转为目标类
     * @param text 源内容
     * @param clazz 目标类
     * @return
     * @param <T> 返回的目标类
     */
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

    /**
     *  将text转为对应的T类型
     *
     *  为什么不用treeToValue: 这里只是兼容判断,因此不存在@Class时的策略应该不报错 --> 所以使用readValue
     * @param text
     * @param path
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T parseObject(String text,String path,Class<T> clazz){
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        try {
            JsonNode jsonNode = objectMapper.readTree(text);
            JsonNode pathNode = jsonNode.path(path);
            return objectMapper.readValue(pathNode.toString(), clazz);
        }catch (IOException e){
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }

    }

    public static <T> T parseObjectQuietly(String json, TypeReference<T> typeReference) {
        try{
            return objectMapper.readValue(json,typeReference);
        }catch (IOException e){
            return null;
        }
    }

    public static<T> T parseObject2(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        return JSONUtil.toBean(text, clazz);
    }
}
