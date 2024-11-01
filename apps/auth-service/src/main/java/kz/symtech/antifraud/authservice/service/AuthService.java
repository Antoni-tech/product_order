package kz.symtech.antifraud.authservice.service;

import kz.symtech.antifraud.authservice.dto.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.symtech.antifraud.feignclients.clients.services.AdminServiceClient;
import kz.symtech.antifraud.models.dto.PrivilegesResponseDTO;
import kz.symtech.antifraud.models.dto.RoleResponseDTO;
import kz.symtech.antifraud.models.dto.UserResponseDTO;
import kz.symtech.antifraud.models.dto.UserTokenValidationResponse;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationExceptionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AdminServiceClient adminServiceClient;

    public RoleResponseDTO authorize(LoginRequest loginRequest, HttpServletResponse res) throws RuntimeException, CatalogOperationException {
        try {
            UserResponseDTO targetUser = adminServiceClient.getUserWithCheckPassword(loginRequest.getLogin(), loginRequest.getPassword()).getBody();
            if (Objects.isNull(targetUser )) {
                throw CatalogOperationExceptionGenerator.generateAuthFailException();
            }
            generateToken(res, "token", loginRequest.getLogin(), 300);
            generateToken(res, "refresh-token", "service-refresh-token", 28800);
            return RoleResponseDTO.builder()
                    .roles(targetUser.getRole().stream().map(role -> RoleResponseDTO.RoleData.builder()
                                    .id(role.getId())
                                    .name(role.getName())
                                    .build())
                            .collect(Collectors.toUnmodifiableSet()))
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw CatalogOperationExceptionGenerator.generateAuthFailException();
        }
    }

    @Transactional
    public UserTokenValidationResponse authenticate(HttpServletRequest req) throws RuntimeException, CatalogOperationException {
        try {
            String jwt = jwtService.getJwtFromRequest(req, "token");
            if (Objects.isNull(jwt)) {
                throw new RuntimeException("No Cookie");
            }
            if (jwtService.validateToken(jwt)) {
                String login = jwtService.getUsernameFrom(jwt);
                UserResponseDTO targetUser = adminServiceClient.getUser(login).getBody();

                List<Long> roleIds = Objects.requireNonNull(targetUser).getRole().stream()
                        .map(RoleResponseDTO.RoleData::getId)
                        .toList();
                RoleResponseDTO roles = adminServiceClient.getRoles(roleIds).getBody();
                Set<PrivilegesResponseDTO.PrivilegesDataDTO> privilegesDataDTOS = new HashSet<>();
                Objects.requireNonNull(roles).getRoles().forEach(role -> privilegesDataDTOS.addAll(role.getPrivileges()));
                return UserTokenValidationResponse.builder()
                        .login(login)
                        .privileges(privilegesDataDTOS)
                        .build();
            }
            throw new RuntimeException("No Cookie");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw CatalogOperationExceptionGenerator.generateAuthFailException();
        }
    }

    public void refresh(HttpServletRequest req, HttpServletResponse res) throws CatalogOperationException {
        try {
            String authToken = jwtService.getJwtFromRequest(req, "token");
            if (Objects.isNull(authToken)) {
                throw new RuntimeException("No token");
            }

            String refreshToken = jwtService.getJwtFromRequest(req, "refresh-token");
            if (refreshToken == null) {
                throw new RuntimeException("No refresh token");
            }
            generateToken(res, "token", jwtService.getUsernameFrom(authToken), 300);
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw CatalogOperationExceptionGenerator.generateAuthFailException();
        }
    }

    public void logout(HttpServletRequest req, HttpServletResponse res) throws CatalogOperationException {
        try {
            deleteTokens(req, res);
        } catch (RuntimeException ex) {
            throw CatalogOperationExceptionGenerator.generateRuntimeException();
        }

    }

    private void generateToken(HttpServletResponse res, String type, String payload, int expiry) throws RuntimeException {
        Cookie cookie = new Cookie(type, jwtService.generateToken(payload));
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        cookie.setHttpOnly(true);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.addCookie(cookie);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private void deleteTokens(HttpServletRequest req, HttpServletResponse res) throws RuntimeException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                res.addCookie(cookie);
            }
    }
}
