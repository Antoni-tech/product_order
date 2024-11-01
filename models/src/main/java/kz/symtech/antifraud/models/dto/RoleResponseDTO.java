package kz.symtech.antifraud.models.dto;

import lombok.*;

import java.util.Set;

/**
 * Данный класс представляет абстракцию над данными
 * о ролях пользователя в системе ТФ
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class RoleResponseDTO {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Data
    public static class RoleData {
        private Long id;

        private String name;

        private Boolean defaultRole;

        private Set<PrivilegesResponseDTO.PrivilegesDataDTO> privileges;
    }

    private Set<RoleData> roles;
}
