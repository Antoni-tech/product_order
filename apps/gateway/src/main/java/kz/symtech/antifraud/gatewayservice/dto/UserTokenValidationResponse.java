package kz.symtech.antifraud.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Данный класс представляет собой
 * абстракцию над данными ответа
 * на запрос поверки валидности токена
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserTokenValidationResponse {
    private String login;

    private Set<PrivilegesResponseDTO.PrivilegesDataDTO> privileges;
}