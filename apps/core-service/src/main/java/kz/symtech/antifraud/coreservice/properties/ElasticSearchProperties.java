package kz.symtech.antifraud.coreservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
public class ElasticSearchProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
