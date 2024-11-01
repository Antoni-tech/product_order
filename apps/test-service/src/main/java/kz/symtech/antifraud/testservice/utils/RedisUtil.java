package kz.symtech.antifraud.testservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisUtil {

    private final ValueOperations<String, Object> valueOperations;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public Object getValue(String key) {
        return valueOperations.get(key);
    }

    public void increment(String key, long delta) {
        valueOperations.increment(key, delta);
    }

}
