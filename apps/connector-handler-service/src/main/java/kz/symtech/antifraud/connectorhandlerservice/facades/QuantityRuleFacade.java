package kz.symtech.antifraud.connectorhandlerservice.facades;

import kz.symtech.antifraud.connectorhandlerservice.dto.ConnectorInputEvaluateData;
import kz.symtech.antifraud.connectorhandlerservice.dto.FieldRelationSubInfoDTO;
import kz.symtech.antifraud.connectorhandlerservice.dto.QuantityRuleDataToProcess;
import kz.symtech.antifraud.connectorhandlerservice.dto.RuleInfo;
import kz.symtech.antifraud.connectorhandlerservice.exceptions.InvalidRuleException;
import kz.symtech.antifraud.connectorhandlerservice.services.RuleEvaluateService;
import kz.symtech.antifraud.models.dto.model.RuleExpressionDTO;
import kz.symtech.antifraud.models.enums.ExpressionType;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuantityRuleFacade {
    private QuantityRuleDataToProcess quantityRuleDataToProcess;
    private final RuleEvaluateService ruleEvaluateService;

    public void executeQuantityRule() {
        ConnectorInputEvaluateData connectorInputEvaluateData = quantityRuleDataToProcess.getConnectorInputEvaluateData();
        ConnectorInputEvaluateData connectorInputEvaluateDataResult = quantityRuleDataToProcess.getConnectorInputEvaluateDataResult();
        Map<String, Object> rulesErrorsMap = quantityRuleDataToProcess.getRulesErrorsMap();
        List<RuleExpressionDTO> ruleExpressionDTOS = quantityRuleDataToProcess.getRuleExpressionDTOS();

        String condition = getCondition(ruleExpressionDTOS, ExpressionType.CONDITION);
        String resultCondition = getCondition(ruleExpressionDTOS, ExpressionType.OPERATION);

        Pair<Boolean, Map<String, Object>> pairAfterEvaluatingRule =
                handleInvalidRuleExceptionForEvaluatingRuleMapMethod(connectorInputEvaluateData, condition);

        if (pairAfterEvaluatingRule.getFirst()) {
            Pair<Map<String, Object>, Map<String, Object>> pairAfterResult =
                    handleInvalidRuleExceptionForEvaluatingResultMapMethod(connectorInputEvaluateData, resultCondition);

            connectorInputEvaluateDataResult.setMap(pairAfterResult.getFirst());

            rulesErrorsMap.putAll(pairAfterResult.getSecond());
        } else {
            rulesErrorsMap.putAll(pairAfterEvaluatingRule.getSecond());
            connectorInputEvaluateDataResult.getMap().put(Fields.RESULT_FIELD, Fields.UNDEFINED);
        }

        addToRuleResults(condition, resultCondition, connectorInputEvaluateDataResult.getMap());
    }

    private void addToRuleResults(String condition, String operation, Map<String, Object> data) {
        List<FieldRelationSubInfoDTO> fieldRelationSubInfoDTOS = quantityRuleDataToProcess.getFieldRelationDTOS()
                .stream()
                .map(f -> FieldRelationSubInfoDTO.builder().var(f.getFieldNameVar()).src(f.getFieldNameSrc()).build())
                .toList();

        RuleInfo ruleInfo = RuleInfo
                .builder()
                .condition(condition)
                .operation(operation)
                .data(data)
                .fieldRelationSubInfoDTOS(fieldRelationSubInfoDTOS)
                .build();

        quantityRuleDataToProcess.getRuleResults().add(ruleInfo);
    }


    private String getCondition(List<RuleExpressionDTO> ruleExpressionDTOS, ExpressionType type) {
        return ruleExpressionDTOS.stream()
                .filter(ruleExpression -> ruleExpression.getType().equals(type))
                .map(RuleExpressionDTO::getExpression)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Type %s not found, rule id - %s",
                        type, quantityRuleDataToProcess.getSDVRuleId())));
    }

    private Pair<Boolean, Map<String, Object>> handleInvalidRuleExceptionForEvaluatingRuleMapMethod(
            ConnectorInputEvaluateData connectorInputEvaluateData,
            String condition) {

        Map<String, Object> rulesErrorsMap = new HashMap<>();
        Boolean res = false;

        try {
            res = ruleEvaluateService.evaluateRuleMap(connectorInputEvaluateData, condition);
        } catch (InvalidRuleException | SpelEvaluationException e) {
            rulesErrorsMap.put(Fields.RULES_FIELD, e.getMessage());
            rulesErrorsMap.put(Fields.RULES_COUNT, 1);
        }

        return Pair.of(res, rulesErrorsMap);
    }

    private Pair<Map<String, Object>, Map<String, Object>> handleInvalidRuleExceptionForEvaluatingResultMapMethod(
            ConnectorInputEvaluateData connectorInputEvaluateData,
            String condition) {

        Map<String, Object> rulesErrorsMap = new HashMap<>();

        try {
            connectorInputEvaluateData = ruleEvaluateService.evaluateResultMap(connectorInputEvaluateData, condition);
        } catch (InvalidRuleException | SpelEvaluationException e) {
            connectorInputEvaluateData.getMap().put(Fields.RESULT_FIELD, Fields.UNDEFINED);
            rulesErrorsMap.put(Fields.RULES_FIELD, e.getMessage());
            rulesErrorsMap.put(Fields.RULES_COUNT, 1);
        }

        return Pair.of(connectorInputEvaluateData.getMap(), rulesErrorsMap);
    }

    public void setQuantityRuleDataToProcess(QuantityRuleDataToProcess quantityRuleDataToProcess) {
        this.quantityRuleDataToProcess = quantityRuleDataToProcess;
    }
}
