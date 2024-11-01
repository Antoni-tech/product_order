package kz.symtech.antifraud.coreservice.entities.rules;

import kz.symtech.antifraud.models.enums.ExpressionType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QualityRuleField {
    private Long id;
    private int number;
    private String textValue;
    private String condition;
    private Boolean toIncidents;
    private Long connectorOutputVersionId;
    private ExpressionType type;
}
