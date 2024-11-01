package kz.symtech.antifraud.mailservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "company")
public class CompanyConfig {
    private String email;
    private String name;
    private String phone;
    private String webSite;
    private String address;
}
