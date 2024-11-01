package kz.symtech.antifraud.coreservice.entities.rules;

import jakarta.persistence.*;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QualityOperation extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule_expression_id", referencedColumnName = "id")
    private RuleExpression ruleExpression;

    private String textValue;
    private int number;
}
