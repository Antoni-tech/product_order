package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/counter")
@RequiredArgsConstructor
public class CounterController {

    private final RedisUtil redisUtil;

    @Value("${redis-prefixes.counter-requests-core}")
    private String counterRequestsCoreRedisPrefix;

    @GetMapping
    public int get() {
        return (int) redisUtil.getValue(counterRequestsCoreRedisPrefix);
    }
}
