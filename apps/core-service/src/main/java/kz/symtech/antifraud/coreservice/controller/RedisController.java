package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisUtil redisUtil;
    @GetMapping
    public Object get(@RequestParam String hashKey,
                      @RequestParam String key) {
        return redisUtil.getValue(hashKey, key);
    }

    @GetMapping("/entries")
    public Map<String, Object> getByHashKey(@RequestParam String hashKey) {
        return redisUtil.getEntries(hashKey);
    }

    @GetMapping("/values")
    public List<Object> getAllValueByHashKey(@RequestParam String hashKey) {
        return redisUtil.getAllValueByHashKey(hashKey);
    }

    @GetMapping("/keys")
    public Set<String> keys() {
        return redisUtil.getKeys();
    }

    @DeleteMapping("/all")
    public void clearAll() {
        redisUtil.deleteAll();
    }
}
