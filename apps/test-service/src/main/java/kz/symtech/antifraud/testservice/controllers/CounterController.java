package kz.symtech.antifraud.testservice.controllers;

import kz.symtech.antifraud.testservice.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/counter")
@RequiredArgsConstructor
public class CounterController {

    private final RedisUtil redisUtil;

    @Value("${redis-prefixes.counter-requests-test}")
    private String counterRequestsTestRedisPrefix;

    @GetMapping
    public int get() {
        Object o = redisUtil.getValue(counterRequestsTestRedisPrefix);
        if (Objects.isNull(o)) {
            return 0;
        }
        return (int) o;
    }
}
