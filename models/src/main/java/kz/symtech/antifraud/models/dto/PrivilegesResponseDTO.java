package kz.symtech.antifraud.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Данный класс представляет собой абстракцию
 * над данными списка привилегий пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class PrivilegesResponseDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class PrivilegesDataDTO {

        private Long id;

        private String name;

        private String tag;

        private String privilegeGroup;
    }
    @JsonProperty("privileges")
    private Set<PrivilegesDataDTO> privilegesDataDTOS;
}
