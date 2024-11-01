package kz.symtech.antifraud.adminservice.handler;

import kz.symtech.antifraud.adminservice.dto.error.ErrorResponseDTO;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Данный класс преобразует исключения
 * к установленному виду структуры, и возвращает ошибочный
 * результат операции в json
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = CatalogOperationException.class)
    protected ResponseEntity<ErrorResponseDTO> handleAuthException(CatalogOperationException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponseDTO.builder().code(ex.getCode()).message(ex.getMessage()).build(), ex.getHttpStatus());
    }
}
