package kz.symtech.antifraud.connectorhandlerservice.dto;

import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.dto.model.RuleExpressionDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class QuantityRuleDataToProcess {
    private Long sDVRuleId;
    private ConnectorInputEvaluateData connectorInputEvaluateData;
    private ConnectorInputEvaluateData connectorInputEvaluateDataResult;
    private Map<String, Object> rulesErrorsMap;
    private List<RuleInfo> ruleResults;
    private List<RuleExpressionDTO> ruleExpressionDTOS;
    private List<FieldRelationDTO> fieldRelationDTOS;
}
