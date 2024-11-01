package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.BaseInitTest;
import kz.symtech.antifraud.coreservice.exceptions.InvalidRuleException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatingServiceTest extends BaseInitTest {

    @Autowired
    private ValidatingService validatingService;

    @Test
    void isValidTest() {
        String validRule = "(clculatedPayment <= 500 OR z < 20)";
        String invalidRule = "(clculatedPayment <= 500 OR z < 20))";

        assertDoesNotThrow(() -> validatingService.validate(validRule));
        assertThrows(InvalidRuleException.class, () -> validatingService.validate(invalidRule));
    }

}
