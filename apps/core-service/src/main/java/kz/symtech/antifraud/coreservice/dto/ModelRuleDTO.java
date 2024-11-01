package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModelRuleDTO {
    private Long id;
    private SummaryDataType type;
    private String name;
    private Long createUserId;
    private Boolean saveResult;
    private SummarySubType ruleType;
}
