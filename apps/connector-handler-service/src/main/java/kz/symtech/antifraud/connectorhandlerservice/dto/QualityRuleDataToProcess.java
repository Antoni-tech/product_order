package kz.symtech.antifraud.connectorhandlerservice.dto;

import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.dto.model.RuleExpressionDTO;
import kz.symtech.antifraud.models.dto.model.SummaryFieldCacheDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class QualityRuleDataToProcess {
    private ConnectorInputEvaluateData connectorInputEvaluateData;
    private ConnectorInputEvaluateData connectorInputEvaluateDataResult;
    private ComponentModel componentModelConnector;
    private Long sDVModelId;
    private Long userId;
    private List<ComponentModel> modelStructComponents;
    private List<FieldRelationDTO> fieldRelationCacheDTOList;
    private List<RuleInfo> ruleResults;
    private Map<String, Object> transaction;
    private List<RuleExpressionDTO> ruleExpressionDTOS;
    private Map<Long, SummaryFieldCacheDTO> summaryFieldMap;
}
