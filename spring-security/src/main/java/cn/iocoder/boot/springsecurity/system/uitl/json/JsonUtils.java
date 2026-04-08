package cn.iocoder.boot.springsecurity.system.uitl.json;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author xiaosheng
 */
@Slf4j
public class JsonUtils {
    static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static String toJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }
    public static <T> T parseObject(@Nullable String text, Class<T> clazz){
        if(StrUtil.isEmpty(text)){
            return null;
        }
        try{
            return objectMapper.readValue(text,clazz);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }
}
