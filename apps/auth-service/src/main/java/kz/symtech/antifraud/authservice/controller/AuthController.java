package kz.symtech.antifraud.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.symtech.antifraud.authservice.dto.LoginRequest;
import kz.symtech.antifraud.authservice.service.AuthService;
import kz.symtech.antifraud.models.dto.RoleResponseDTO;
import kz.symtech.antifraud.models.dto.UserTokenValidationResponse;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    final private AuthService authService;

    @PostMapping("/user")
    public ResponseEntity<RoleResponseDTO> authorize(@RequestBody LoginRequest loginRequest, HttpServletResponse res) throws CatalogOperationException {
        RoleResponseDTO result = authService.authorize(loginRequest, res);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate")
    public ResponseEntity<UserTokenValidationResponse> authenticate(HttpServletRequest req) throws CatalogOperationException {
        UserTokenValidationResponse result = authService.authenticate(req);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest req, HttpServletResponse res) throws CatalogOperationException {
        authService.refresh(req, res);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) throws CatalogOperationException {
        authService.logout(req, res);
        return ResponseEntity.ok().build();
    }
}