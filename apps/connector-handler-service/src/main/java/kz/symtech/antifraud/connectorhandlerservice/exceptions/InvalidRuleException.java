package kz.symtech.antifraud.connectorhandlerservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
public class InvalidRuleException extends RuntimeException {
    private final String message;
}
