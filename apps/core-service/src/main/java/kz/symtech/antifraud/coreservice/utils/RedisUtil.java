package kz.symtech.antifraud.coreservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;
    private final HashOperations<String, String, Object> hashOperations;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void putValue(String hashKey, String key, Object value) {
        try {
            hashOperations.put(hashKey, key, value);
        } catch (Exception e) {
            log.error(String.format("Can't put data due to %s", e.getMessage()));
        }
    }

    public void putAllValues(String hashKey, Map<String, Object> data) {
        try {
            hashOperations.putAll(hashKey, data);
        } catch (Exception e) {
            log.error(String.format("Can't put data due to %s", e.getMessage()));
        }
    }

    public Object getValue(String hashKey, String key) {
        try {
            return hashOperations.get(hashKey, key);
        } catch (Exception e) {
            log.error(String.format("Can't get value due to %s", e.getMessage()));
        }
        return null;
    }

    public Object getValue(String key) {
        return valueOperations.get(key);
    }

    public void delete(String hashKey, String key) {
        try {
            hashOperations.delete(hashKey, key);
        } catch (Exception e) {
            log.error(String.format("Can't delete object(s) due to %s", e.getMessage()));
        }
    }

    public List<Object> getAllValueByHashKey(String hashKey) {
        try {
            return hashOperations.values(hashKey);
        } catch (Exception e) {
            log.error(String.format("Can't get values due to %s", e.getMessage()));
        }
        return null;
    }

    public List<Object> getAllValueByHashKeyAndKeys(String hashKey, List<String> keys) {
        try {
            return hashOperations.multiGet(hashKey, keys);
        } catch (Exception e) {
            log.error(String.format("Can't get values due to %s", e.getMessage()));
        }
        return null;
    }

    public void clearByHashKey(String hashKey) {
        try {
            redisTemplate.delete(hashKey);
        } catch (Exception e) {
            log.error(String.format("Can't delete object(s) due to %s", e.getMessage()));
        }
    }

    public Map<String, Object> getEntries(String hashKey) {
        return hashOperations.entries(hashKey);
    }

    public Set<String> getKeys() {
        return redisTemplate.keys("*");
    }

    public void deleteAll() {
        Set<String> keys = getKeys();
        redisTemplate.delete(keys);
    }
}
