package kz.symtech.antifraud.mailservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Данный класс содержит адрес разрешенного сервиса,
 * откуда может быть сделан запроc
 */
@Data
@Component
@ConfigurationProperties(prefix = "connection")
public class OriginConfig {
    private String allowedOrigin;
}
