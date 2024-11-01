package kz.symtech.antifraud.models.entity.exception;

import org.springframework.http.HttpStatus;

public class CatalogOperationExceptionGenerator {

    public static CatalogOperationException generateIncorrectRequest() {
        return CatalogOperationException.builder()
                .code(10110)
                .message("Incorrect request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    public static CatalogOperationException generateAuthFailException() {
        return CatalogOperationException.builder()
                .code(10110)
                .message("Authorization error")
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
    }

    public static CatalogOperationException generateCheckPasswordFailException() {
        return CatalogOperationException.builder()
                .code(10111)
                .message("Check Password")
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

    public static CatalogOperationException generateDBException() {
        return CatalogOperationException.builder()
                .code(10101)
                .message("TF DB error")
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }

    public static CatalogOperationException generateDataNotFoundException() {
        return CatalogOperationException.builder()
                .code(10001)
                .message("Data not found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    public static CatalogOperationException generateExceptionWithContext(String ctx) {
        return CatalogOperationException.builder()
                .code(10110)
                .message(ctx)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    public static CatalogOperationException generateActiveDirectoryNotFoundException() {
        return CatalogOperationException.builder()
                .code(10103)
                .message("Data not found in AD")
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .build();
    }
}
