package kz.symtech.antifraud.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Данный класс представлет собой абстракцию над
 * списком пользователей системы ТФ
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserListDTO {
    @JsonProperty("users")
    private Set<UserResponseDTO> userResponseDTOS;
}
