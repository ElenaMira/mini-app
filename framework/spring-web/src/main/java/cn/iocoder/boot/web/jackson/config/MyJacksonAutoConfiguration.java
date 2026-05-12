package cn.iocoder.boot.web.jackson.config;

import cn.iocoder.boot.common.uitl.json.JsonUtils;
import cn.iocoder.boot.common.uitl.json.databind.TimestampLocalDateTimeDeserializer;
import cn.iocoder.boot.common.uitl.json.databind.TimestampLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author xiaosheng
 */
@AutoConfiguration(after = JacksonAutoConfiguration.class)
@Slf4j
public class MyJacksonAutoConfiguration {
    /**
     * 从 Builder 源头定制（关键：使用 *ByType，避免 handledType 要求）
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer ldtEpochMillisCustom(){
        // 优先级最高的类型序列化器
        //从源头绑定类型 → 对 Spring 管理的 ObjectMapper 生效
        return builder->builder
//                // Long -> Number
//                .serializerByType(Long.class, NumberSerializer.INSTANCE)
//                .serializerByType(Long.TYPE, NumberSerializer.INSTANCE)
                .serializerByType(LocalDate.class, LocalDateSerializer.INSTANCE)
                .deserializerByType(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .serializerByType(LocalTime.class, LocalTimeSerializer.INSTANCE)
                .deserializerByType(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                // LocalDateTime < - > EpochMillis
                .serializerByType(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
                .deserializerByType(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
    }
    /**
     * 以 Bean 形式暴露 Module（Boot 会自动注册到所有 ObjectMapper）
     */
    @Bean
    public Module timestampSupportModuleBean(){
        //Jackson 标准扩展方式,通用性最强支持第三方库
        SimpleModule m = new SimpleModule("TimestampSupportModule");
        // Long -> Number，避免前端精度丢失 todo: 新增
//        m.addSerializer(Long.class, NumberSerializer.INSTANCE);
//        m.addSerializer(Long.TYPE, NumberSerializer.INSTANCE);
        // LocalDate / LocalTime
        m.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        m.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        m.addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
        m.addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
        // LocalDateTime < - > EpochMillis
        m.addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE);
        m.addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
        return m;
    }

    /**
     * 初始化全局 JsonUtils，直接使用主 ObjectMapper
     */
    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
        public JsonUtils jsonUtils(ObjectMapper objectMapper){
        JsonUtils.init(objectMapper);
        log.debug("[init][初始化 JsonUtils 成功]");
        return new JsonUtils();
    }
}
