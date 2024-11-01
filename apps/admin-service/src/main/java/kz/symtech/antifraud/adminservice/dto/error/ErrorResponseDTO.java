package kz.symtech.antifraud.adminservice.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данный класс содержит абстракцию
 * над данными ошибки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {
    private int code;
    private String message;
}
