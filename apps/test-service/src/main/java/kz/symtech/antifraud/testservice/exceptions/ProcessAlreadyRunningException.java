package kz.symtech.antifraud.testservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProcessAlreadyRunningException extends RuntimeException {
    public ProcessAlreadyRunningException(String message) {
        super(message);
    }
}
