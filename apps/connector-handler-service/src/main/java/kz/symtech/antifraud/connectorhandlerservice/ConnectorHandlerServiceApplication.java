package kz.symtech.antifraud.connectorhandlerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "kz.symtech.antifraud.feignclients.clients.services")
public class ConnectorHandlerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectorHandlerServiceApplication.class, args);
    }

}
