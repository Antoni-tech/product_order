package kz.symtech.antifraud.models.entity.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogOperationException extends Exception{

    private HttpStatus httpStatus;
    private int code;
    private String message;
}


