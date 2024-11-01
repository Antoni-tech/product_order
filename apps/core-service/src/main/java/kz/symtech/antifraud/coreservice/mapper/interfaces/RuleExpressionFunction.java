package kz.symtech.antifraud.coreservice.mapper.interfaces;

import kz.symtech.antifraud.coreservice.entities.rules.RuleExpression;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.RuleExpressionDTO;

@FunctionalInterface
public interface RuleExpressionFunction {
    RuleExpressionDTO apply(RuleExpression ruleExpression, ComponentModel componentModel);
}
