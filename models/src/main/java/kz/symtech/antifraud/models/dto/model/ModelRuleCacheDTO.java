package kz.symtech.antifraud.models.dto.model;

import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModelRuleCacheDTO implements Serializable {
    private Long id;
    private Long summaryDataVersionId;
    private SummarySubType type;
    private List<Long> summaryFieldRuleIds;
    private List<RuleExpressionDTO> ruleExpressionDTOS;
}
