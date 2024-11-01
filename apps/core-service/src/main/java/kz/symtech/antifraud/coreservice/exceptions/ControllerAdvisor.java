package kz.symtech.antifraud.coreservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import kz.symtech.antifraud.coreservice.dto.ApiErrorResponseDTO;
import kz.symtech.antifraud.coreservice.dto.ExceptionResponseMessage;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConnectorNotFoundException.class)
    public ResponseEntity<ExceptionResponseMessage> handleExistsException(ConnectorNotFoundException ex) {
        return new ResponseEntity<>(ex.getExceptionResponseMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleExistsException(NotFoundException ex, final HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRuleException.class)
    public ResponseEntity<ExceptionResponseMessage> handleExistsException(InvalidRuleException ex) {
        return new ResponseEntity<>(ex.getExceptionResponseMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(final ApplicationException exception, final HttpServletRequest request) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        log.error(exception.getMessage());
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    @ExceptionHandler(FieldAlreadyExistException.class)
    public ResponseEntity<?> handleApplicationException(final FieldAlreadyExistException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());

        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdenticalValueException.class)
    public ResponseEntity<?> handleApplicationException(final IdenticalValueException exception, final HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("path", request.getRequestURI());

        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectFieldTypeException.class)
    public ResponseEntity<?> handleApplicationException(final IncorrectFieldTypeException exception, final HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("path", request.getRequestURI());

        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RelationEstablishException.class)
    public ResponseEntity<?> handleApplicationException(final RelationEstablishException exception, final HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errors", exception.getRelationErrors());
        body.put("path", request.getRequestURI());

        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionInputDataException.class)
    public ResponseEntity<?> handleApplicationException(final TransactionInputDataException exception, final HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errors", exception.getTransactionInputErrors());
        body.put("path", request.getRequestURI());

        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncompleteDataException.class)
    public ResponseEntity<?> handleApplicationException(final IncompleteDataException exception, final HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errors", exception.getMessages());
        body.put("path", request.getRequestURI());

        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SummaryDataVersionNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleSummaryDataVersionNotExistsException(final SummaryDataVersionNotFoundException exception, HttpServletRequest request) {
        String exceptionMessage = String.format("Summary data version not found: %d", exception.getVersionId());

        ApiErrorResponseDTO response = new ApiErrorResponseDTO(
                exceptionMessage,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );

        log.error(exceptionMessage);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleUnexpectedException(Exception exception, HttpServletRequest request) {
//        ApiErrorResponseDTO response = new ApiErrorResponseDTO(
//                exception.getMessage(),
//                HttpStatus.NOT_FOUND.value(),
//                HttpStatus.NOT_FOUND.name(),
//                request.getRequestURI(),
//                request.getMethod(),
//                LocalDateTime.now()
//        );
//
//        exception.printStackTrace();
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
