package kz.symtech.antifraud.coreservice.exceptions;

import kz.symtech.antifraud.coreservice.dto.ExceptionResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
public class ConnectorNotFoundException extends RuntimeException{
    private ExceptionResponseMessage exceptionResponseMessage;
}
