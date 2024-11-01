package kz.symtech.antifraud.coreservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldAlreadyExistException extends RuntimeException {
    private final String message;
}
