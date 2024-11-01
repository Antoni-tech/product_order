package kz.symtech.antifraud.models.dto.model;

import kz.symtech.antifraud.models.enums.ExpressionType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleExpressionDTO implements Serializable {
    private String expression;
    private ExpressionType type;
    private QualityOperationDTO qualityOperationDTO;
}
