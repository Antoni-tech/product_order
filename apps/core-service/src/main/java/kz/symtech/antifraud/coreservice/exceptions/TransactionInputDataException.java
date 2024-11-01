package kz.symtech.antifraud.coreservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionInputDataException extends RuntimeException {
    private final List<String> transactionInputErrors;
}
