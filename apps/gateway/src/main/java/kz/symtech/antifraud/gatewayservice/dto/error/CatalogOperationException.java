package kz.symtech.antifraud.gatewayservice.dto.error;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * Данный класс представляет собой абстракцию над
 * данными ошибки
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CatalogOperationException extends Exception{

    private HttpStatus httpStatus;
    private int code;
    private String message;
}

