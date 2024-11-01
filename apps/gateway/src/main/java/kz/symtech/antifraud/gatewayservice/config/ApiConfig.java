package kz.symtech.antifraud.gatewayservice.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "api")
@Slf4j
public class ApiConfig {
    private String authServiceUrl;
    private List<String> allowedOrigins;

    @PostConstruct
    public void init() {
        logAllowedOrigins();
    }

    public void logAllowedOrigins() {
        log.info("Allowed Origins: {}", allowedOrigins);
    }
}
