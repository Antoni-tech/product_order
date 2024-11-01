package kz.symtech.antifraud.models.dto;

import lombok.*;

import java.util.Set;

/**
 * Данный класс представялет абстракцию
 * над данными, возвращаемыми при вализации токена пользователя
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
