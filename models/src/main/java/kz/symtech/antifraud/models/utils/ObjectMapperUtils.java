package kz.symtech.antifraud.models.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.util.Map;

public class ObjectMapperUtils {
    public static ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setDateFormat(new StdDateFormat());

    public static <T> T treeToValue(JsonNode node, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(JsonNode node, TypeReference<T> tTypeReference) {
        try {
            return objectMapper.readValue(node.traverse(), tTypeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> fromJsonStringToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> fromObjectToMap(Object object) {
        return objectMapper.convertValue(object, new TypeReference<>() {
        });
    }

    public static JsonNode fromStringToJsonNode(String string) {
        try {
            return objectMapper.readTree(string);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("The given string value: " + string + " cannot be transformed to JsonNode object", e);
        }
    }

    public static String getJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, R> R convertValue(T object, TypeReference<R> typeReference) {
        return objectMapper.convertValue(object, typeReference);
    }
}
