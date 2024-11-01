package kz.symtech.antifraud.gatewayservice.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данный класс представляет собой абстракцию над данными
 * ощибки, возвращаемой с сервера
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO extends Exception{

    private int code;

    private String message;
}
