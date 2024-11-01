package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.dto.ExceptionResponseMessage;
import kz.symtech.antifraud.coreservice.exceptions.InvalidRuleException;
import kz.symtech.antifraud.coreservice.services.ValidatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidatingServiceImpl implements ValidatingService {

    private static boolean isValid(String rule) {
        ExpressionParser parser = new SpelExpressionParser();
        try {
            if (!rule.isBlank()) {
                parser.parseExpression(rule);
            }
            return true;
        } catch (SpelParseException e) {
            return false;
        }
    }

    @Override
    public void validate(String rule) {
        if (!isValid(rule)) {
            throw new InvalidRuleException(new ExceptionResponseMessage("Invalid rule"));
        }
    }

}