package kz.symtech.antifraud.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Данный класс опредставляет собой абстракцию
 * над данными привилегий
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

        @JsonProperty("id")
        private Long id;

        @JsonProperty("NAME")
        private String name;
    }
    @JsonProperty("PRIVILEGES")
    private Set<PrivilegesDataDTO> privilegesDataDTOS;
}
