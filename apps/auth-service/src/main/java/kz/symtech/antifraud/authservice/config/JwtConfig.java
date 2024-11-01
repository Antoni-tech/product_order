package kz.symtech.antifraud.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Данный класс предоставляет собой абтракцию над
 * данными о путе к публичным и приватным
 * ключам для создания и валидации JWT токена
 * а также данные о сроке жизни токена
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private Long expirationInMinutes;
    private String privateKeyPath;
    private String publicKeyPath;
}
