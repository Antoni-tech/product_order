package kz.symtech.antifraud.coreservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IncorrectFieldTypeException extends RuntimeException{
    private final String message;
}
