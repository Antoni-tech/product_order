package kz.symtech.antifraud.mailservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "kz.symtech.antifraud.feignclients")
public class MailServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}
}
