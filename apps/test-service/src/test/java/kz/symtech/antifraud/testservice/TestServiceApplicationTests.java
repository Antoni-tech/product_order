package kz.symtech.antifraud.testservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
class TestServiceApplicationTests {

    @Test
    void contextLoads() {
        LocalDateTime l = LocalDateTime.now();
        log.info(l.toString());
    }

}
