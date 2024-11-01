package kz.symtech.antifraud.gatewayservice.helpers;

import kz.symtech.antifraud.gatewayservice.dto.error.CatalogOperationException;
import org.springframework.http.HttpStatus;


/**
 * Дaнный класс отвечает за генерацию ошибок
 *
 */
public class CatalogOperationExceptionGenerator {

    public static CatalogOperationException generateIncorrectRequest (){
        return CatalogOperationException.builder()
                .code(10110)
                .message("Bad request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    public static CatalogOperationException generateAuthFailException(){
        return CatalogOperationException.builder()
                .code(10110)
                .message("Error authorized")
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
    }

    public static CatalogOperationException generateRuntimeException() {
        return CatalogOperationException.builder()
                .code(10099)
                .message("Internal server error")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static CatalogOperationException generateExceptionWithContext(String ctx) {
        return CatalogOperationException.builder()
                .code(10110)
                .message(ctx)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
}
