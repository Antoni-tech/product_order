package kz.symtech.antifraud.coreservice.entities.rules;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuantityRule {
    private String condition;
    private String resultCondition;
}
