package kz.symtech.antifraud.testservice.annotations.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kz.symtech.antifraud.testservice.annotations.ValidDelay;
import kz.symtech.antifraud.testservice.dto.TransactionDataRequestDto;
import kz.symtech.antifraud.testservice.exceptions.InvalidDelayException;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Objects;

public class DelayValidator implements ConstraintValidator<ValidDelay, TransactionDataRequestDto> {

    @Override
    public void initialize(ValidDelay constraintAnnotation) {
    }

    @Override
    public boolean isValid(TransactionDataRequestDto transactionDataRequestDto, ConstraintValidatorContext context) {
        if (transactionDataRequestDto == null) {
            return true;
        }

        int delay = transactionDataRequestDto.getDelay();
        ChronoUnit chronoUnit = transactionDataRequestDto.getChronoUnit();
        validateDelayAndChronoUnit(delay, chronoUnit);

        if (transactionDataRequestDto.getDelayToReplace() != 0
                && Objects.nonNull(transactionDataRequestDto.getChronoUnitToReplace())) {
            int delayToReplace = transactionDataRequestDto.getDelayToReplace();
            ChronoUnit chronoUnitToReplace = transactionDataRequestDto.getChronoUnitToReplace();
            validateDelayAndChronoUnit(delayToReplace, chronoUnitToReplace);
        }

        return true;
    }

    private void validateDelayAndChronoUnit(int delay, ChronoUnit chronoUnit) {
        if (chronoUnit == ChronoUnit.HOURS && (delay < 1 || delay > 23)) {
            throw new InvalidDelayException(Collections.singletonMap("delay",
                    "Invalid delay for HOURS, value must be in range 1-23"));
        } else if ((chronoUnit == ChronoUnit.SECONDS || chronoUnit == ChronoUnit.MINUTES) && (delay < 1 || delay > 59)) {
            throw new InvalidDelayException(Collections.singletonMap("delay",
                    String.format("Invalid delay for %s, value must be in range 1-59", chronoUnit)));
        }
    }
}
