package kz.symtech.antifraud.models.exceptions;

import org.springframework.http.HttpStatus;

public class RetriableException extends ResponseStatusException {

    public RetriableException(int rawStatusCode, String code, String reason) {
        super(rawStatusCode, code, reason);
    }

    public RetriableException(HttpStatus status, String code, String reason) {
        super(status, code, reason);
    }

    public RetriableException(HttpStatus status, String code, String reason, Throwable cause) {
        super(status, code, reason, cause);
    }

    public RetriableException(int rawStatusCode, String code, String reason, Throwable cause) {
        super(rawStatusCode, code, reason, cause);
    }
}
