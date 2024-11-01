package kz.symtech.antifraud.testservice.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class InvalidDelayException extends RuntimeException {
    private final Map<String, String> errors;
}
