package kz.symtech.antifraud.adminservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "connection")
public class OriginConfig{
    private String allowedOrigin;
}
