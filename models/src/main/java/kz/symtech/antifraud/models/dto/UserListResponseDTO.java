package kz.symtech.antifraud.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Данный класс представлет собой абстракцию над
 * списком пользователей системы ТФ
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserListResponseDTO {

    private List<UserResponseDTO> users;

    private Long count;
}
