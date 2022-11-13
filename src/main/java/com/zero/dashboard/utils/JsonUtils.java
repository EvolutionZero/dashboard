package com.zero.dashboard.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public abstract class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("JSON数据({})转化为类文件（{}）异常：", new Object[]{json, clazz, e});
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转化为JSON文本出错：{}, {}", object.getClass(), object.toString());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(List.class, clazz));
        } catch (IOException e) {
            log.error("JSON数据({})转化为类文件（{}）异常：", json, clazz, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}