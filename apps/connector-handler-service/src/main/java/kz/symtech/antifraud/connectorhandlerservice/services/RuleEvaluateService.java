package kz.symtech.antifraud.connectorhandlerservice.services;

import kz.symtech.antifraud.connectorhandlerservice.dto.*;
import kz.symtech.antifraud.models.dto.model.SummaryFieldCacheDTO;

import java.util.List;
import java.util.Map;

public interface RuleEvaluateService {
    Map<String, Object> validateFields(List<SummaryFieldCacheDTO> summaryFields, Map<String, Object> data);

    Boolean evaluateRuleMap(ConnectorInputEvaluateData connectorInputEvaluateData, String rule);

    ConnectorInputEvaluateData evaluateResultMap(ConnectorInputEvaluateData validatedConnectorInputPairDTO, String rule);

    QuantityTestResponseDTO testQuantityRule(QuantityTestRequestDTO quantityTestRequestDTO);

    List<QualityTestResponseDTO> testQualityRule(QualityTestRequestDTO qualityTestRequestDTO);

}
