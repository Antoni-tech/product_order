package kz.symtech.antifraud.feignclients.clients.services;

import kz.symtech.antifraud.models.dto.UserTokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    @PostMapping("/auth-service/api/auth/validate")
    ResponseEntity<UserTokenValidationResponse> authenticate();
}
