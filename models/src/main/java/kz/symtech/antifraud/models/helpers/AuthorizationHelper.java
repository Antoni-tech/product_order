package kz.symtech.antifraud.models.helpers;

import kz.symtech.antifraud.models.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@Service
@Slf4j
public class AuthorizationHelper {
    private final RestTemplate restTemplate;
    private final String URL = "http://auth-service:8070";
    public final Logger logger = LoggerFactory.getLogger(AuthorizationHelper.class);
    public AuthorizationHelper() {
        this.restTemplate = restTemplate();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    public boolean checkIfAuthorized(String credentials) {
        //TODO: Refactor this ASAP
        String[] credentialsArray = new String(Base64.getDecoder().decode(credentials.split(" ")[1])).split(":");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginRequest loginRequest = LoginRequest.builder()
                .login(credentialsArray[0])
                .password(credentialsArray[1])
                .build();
        HttpEntity<String> request = new HttpEntity<>(loginRequest.toString(), headers);
        ResponseEntity<Object> req = restTemplate.exchange(this.URL, HttpMethod.POST, request, Object.class);
        return req.getStatusCode() != HttpStatus.OK;
    }

}
