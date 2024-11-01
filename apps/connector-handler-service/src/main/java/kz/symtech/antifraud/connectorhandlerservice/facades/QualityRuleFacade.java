package kz.symtech.antifraud.connectorhandlerservice.facades;

import com.fasterxml.jackson.core.type.TypeReference;
import kz.symtech.antifraud.connectorhandlerservice.dto.*;
import kz.symtech.antifraud.connectorhandlerservice.entities.Incident;
import kz.symtech.antifraud.connectorhandlerservice.enums.TransactionInputDataType;
import kz.symtech.antifraud.connectorhandlerservice.exceptions.InvalidRuleException;
import kz.symtech.antifraud.connectorhandlerservice.services.IncidentService;
import kz.symtech.antifraud.connectorhandlerservice.services.RuleEvaluateService;
import kz.symtech.antifraud.connectorhandlerservice.services.TransactionInputDataService;
import kz.symtech.antifraud.connectorhandlerservice.utils.RedisUtil;
import kz.symtech.antifraud.connectorhandlerservice.utils.TransactionDataUtil;
import kz.symtech.antifraud.feignclients.clients.services.CoreServiceClient;
import kz.symtech.antifraud.feignclients.clients.services.TestServiceClient;
import kz.symtech.antifraud.models.dto.IncidentResponseDTO;
import kz.symtech.antifraud.models.dto.model.*;
import kz.symtech.antifraud.models.enums.ExpressionType;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kz.symtech.antifraud.models.enums.Fields.DEFAULT_COUNTER_VALUE;
import static kz.symtech.antifraud.models.enums.Fields.ERRORS_FIELD;

@Slf4j
@Component
@RequiredArgsConstructor
public class QualityRuleFacade {
    private static final int DEFAULT_NUMBER = 999;

    private final RuleEvaluateService ruleEvaluateService;
    private final IncidentService incidentService;
    private final TestServiceClient testServiceClient;
    private final CoreServiceClient coreServiceClient;
    private final RedisUtil redisUtil;
    private final TransactionInputDataService transactionInputDataService;
    @Setter
    private QualityRuleDataToProcess qualityRuleDataToProcess;

    @Value("${redis-prefixes.transaction-input}")
    private String transactionInputRedisPrefix;

    @Value("${redis-prefixes.amount-of-transactions}")
    private String amountOfTransactionRedisPrefix;

    @Value("${redis-prefixes.amount-of-transaction-errors}")
    private String amountOfTransactionErrorsRedisPrefix;

    public void handleQualityRule() {
        List<RuleExpressionDTO> ruleExpressionDTOS = qualityRuleDataToProcess.getRuleExpressionDTOS();

        Pair<String, QualityOperationDTO> pair =
                executeQualityRule(ruleExpressionDTOS, qualityRuleDataToProcess.getConnectorInputEvaluateData());

        QualityOperationDTO qualityOperationDTO = pair.getSecond();
        String condition = pair.getFirst();

        ComponentModel summaryDataVersionConnector = qualityRuleDataToProcess.getComponentModelConnector();

        if (qualityOperationDTO.getToIncidents()) {

            qualityRuleDataToProcess.getConnectorInputEvaluateData().getMap()
                    .putAll(qualityRuleDataToProcess.getConnectorInputEvaluateDataResult().getMap());

            Long sDVConnectorOutputId = qualityOperationDTO.getSummaryDataVersionConnectorOutputId();

            addToRuleResults(condition, qualityRuleDataToProcess.getConnectorInputEvaluateData().getMap());

            Incident incident = createIncident(
                    qualityRuleDataToProcess.getSDVModelId(),
                    summaryDataVersionConnector.getSummaryDataVersionId(),
                    qualityOperationDTO,
                    qualityRuleDataToProcess.getUserId(),
                    qualityRuleDataToProcess.getRuleResults()
            );
            incidentService.save(incident);

            Map<String, Object> resultMap = getResultMap(sDVConnectorOutputId);
            log.info(Strings.repeat("-", 100));
            log.info("Result connector output map: " + resultMap);
            log.info(String.format("If map is empty, it means that incident's textValue is %s", Fields.UNDEFINED));
            log.info(Strings.repeat("-", 100));

            send(sDVConnectorOutputId, qualityRuleDataToProcess.getModelStructComponents(), incident, resultMap);
        }
    }

    private void addToRuleResults(String condition, Map<String, Object> data) {
        RuleInfo ruleInfo = RuleInfo
                .builder()
                .condition(condition)
                .operation(null)
                .data(data)
                .build();

        qualityRuleDataToProcess.getRuleResults().add(ruleInfo);
    }

    private Map<String, Object> getResultMap(Long sDVConnectorOutputId) {
        List<FieldRelationDTO> fieldRelationsConnectorOutput =
                qualityRuleDataToProcess.getFieldRelationCacheDTOList()
                        .stream()
                        .filter(f -> f.getSummaryDataVersionId().equals(sDVConnectorOutputId))
                        .toList();

        Set<SummaryFieldCacheDTO> summaryFieldCacheSrcDTOSet = new HashSet<>();
        TransactionDataUtil.fillSummaryFields(
                summaryFieldCacheSrcDTOSet, qualityRuleDataToProcess.getSummaryFieldMap(), fieldRelationsConnectorOutput, true);

        Set<SummaryFieldCacheDTO> summaryFieldCacheVarDTOSet = new HashSet<>();
        TransactionDataUtil.fillSummaryFields(
                summaryFieldCacheVarDTOSet, qualityRuleDataToProcess.getSummaryFieldMap(), fieldRelationsConnectorOutput, false);

        Map<Long, FieldRelationDTO> fieldRelationCacheDTOMap = convertListToMap(fieldRelationsConnectorOutput);

        return buildConnectorOutputNestedData(
                qualityRuleDataToProcess.getComponentModelConnector().getSummaryDataVersionId(),
                summaryFieldCacheSrcDTOSet.stream().toList(),
                summaryFieldCacheVarDTOSet.stream().toList(),
                fieldRelationCacheDTOMap);
    }

    private Map<Long, FieldRelationDTO> convertListToMap(List<FieldRelationDTO> fieldRelationCacheDTOList) {
        return fieldRelationCacheDTOList
                .stream()
                .collect(Collectors.toMap(
                        FieldRelationDTO::getSummaryFieldId,
                        Function.identity()
                ));
    }

    private Map<String, Object> buildConnectorOutputNestedData(
            Long sDVConnectorInputId,
            List<SummaryFieldCacheDTO> summaryFieldCacheSrcDTOList,
            List<SummaryFieldCacheDTO> summaryFieldCacheVarDTOList,
            Map<Long, FieldRelationDTO> fieldRelationCacheDTOMap) {

        Map<String, Object> map = new HashMap<>();

        List<SummaryFieldCacheDTO> fieldsWithNoParents = summaryFieldCacheVarDTOList
                .stream()
                .filter(s -> Objects.isNull(s.getParentSummaryField()))
                .toList();

        fieldsWithNoParents.forEach(f -> {
            if (f.getType().equals(SummaryFieldType.OBJECT)) {
                Map<String, Object> subMap = new HashMap<>();
                buildSubData(
                        summaryFieldCacheSrcDTOList,
                        summaryFieldCacheVarDTOList,
                        f,
                        subMap,
                        fieldRelationCacheDTOMap,
                        sDVConnectorInputId
                );
                map.put(f.getName(), subMap);
            } else {
                FieldRelationDTO fieldRelation = fieldRelationCacheDTOMap.get(f.getId());
                fillMap(fieldRelation, summaryFieldCacheSrcDTOList, map, sDVConnectorInputId);
            }
        });

        return map;
    }

    private void fillMap(
            FieldRelationDTO fieldRelation,
            List<SummaryFieldCacheDTO> summaryFieldCacheSrcDTOList,
            Map<String, Object> map,
            Long sDVConnectorInputId) {

        switch (fieldRelation.getSourceType()) {
            case RULE -> {
                String key = fieldRelation.getSummaryDataVersionModelId() +
                        ":" + sDVConnectorInputId +
                        ":" + fieldRelation.getSDVSourceId();

                TransactionInputDataDTORule transactionInputDataRule = (TransactionInputDataDTORule)
                        redisUtil.getValue(transactionInputRedisPrefix + ":" + TransactionInputDataType.RULE, key);
                if (Objects.isNull(transactionInputDataRule)) {
                    transactionInputDataRule = transactionInputDataService.
                            get(fieldRelation.getSummaryDataVersionModelId(), sDVConnectorInputId, fieldRelation.getSDVSourceId());
                }

                map.put(fieldRelation.getFieldNameVar(), transactionInputDataRule.getData().get(fieldRelation.getFieldNameSrc()));
            }
            case CONNECTOR_INPUT -> {
                Map<String, Object> transaction;

                if (sDVConnectorInputId.equals(fieldRelation.getSDVSourceId())) {
                    transaction = new HashMap<>(qualityRuleDataToProcess.getTransaction());
                } else {
                    String hashKey = transactionInputRedisPrefix + ":" + TransactionInputDataType.CONNECTOR;
                    String key = fieldRelation.getSummaryDataVersionModelId() + ":" + fieldRelation.getSDVSourceId();

                    TransactionInputDataDTO transactionInputDataDTO = (TransactionInputDataDTO) redisUtil.getValue(hashKey, key);
                    if (Objects.isNull(transactionInputDataDTO)) {
                        transactionInputDataDTO = transactionInputDataService
                                .get(fieldRelation.getSummaryDataVersionModelId(), fieldRelation.getSDVSourceId(), null);
                    }

                    transaction = transactionInputDataDTO.getData();
                }

                fillSubMap(transaction, fieldRelation, summaryFieldCacheSrcDTOList, map);
            }
            default ->
                    throw new IllegalArgumentException(String.format("Unknown source type: %s", fieldRelation.getSourceType()));
        }
    }

    private void buildSubData(
            List<SummaryFieldCacheDTO> summaryFieldCacheSrcDTOList,
            List<SummaryFieldCacheDTO> summaryFieldCacheVarDTOList,
            SummaryFieldCacheDTO summaryFieldCacheDTO,
            Map<String, Object> subMap,
            Map<Long, FieldRelationDTO> fieldRelationCacheDTOMap,
            Long sDVConnectorInputId) {

        List<SummaryFieldCacheDTO> children = summaryFieldCacheVarDTOList
                .stream()
                .filter(s -> Objects.nonNull(s.getParentSummaryField()))
                .filter(s -> s.getParentSummaryField().getId().equals(summaryFieldCacheDTO.getId()))
                .toList();

        children.forEach(c -> {
            if (c.getType().equals(SummaryFieldType.OBJECT)) {
                Map<String, Object> subMapNested = new HashMap<>();
                buildSubData(
                        summaryFieldCacheSrcDTOList,
                        summaryFieldCacheVarDTOList,
                        c,
                        subMapNested,
                        fieldRelationCacheDTOMap,
                        sDVConnectorInputId
                );
                subMap.put(c.getName(), subMapNested);
            } else {
                FieldRelationDTO fieldRelation = fieldRelationCacheDTOMap.get(c.getId());
                fillMap(fieldRelation, summaryFieldCacheSrcDTOList, subMap, sDVConnectorInputId);
            }
        });
    }

    private void fillSubMap(
            Map<String, Object> transaction,
            FieldRelationDTO fieldRelationCacheDTO,
            List<SummaryFieldCacheDTO> summaryFieldCacheDTOList,
            Map<String, Object> subMap) {

        Stack<Pair<String, SummaryFieldType>> keys = new Stack<>();
        TransactionDataUtil.fillStack(keys, summaryFieldCacheDTOList, fieldRelationCacheDTO.getSourceId());

        while (!keys.isEmpty()) {
            Pair<String, SummaryFieldType> p = keys.pop();
            String key = p.getFirst();
            SummaryFieldType type = p.getSecond();

            Object value;
            if (type.equals(SummaryFieldType.OBJECT)) {
                transaction = ObjectMapperUtils.fromObjectToMap(transaction.get(key));
            } else {
                value = transaction.get(fieldRelationCacheDTO.getFieldNameSrc());
                subMap.put(fieldRelationCacheDTO.getFieldNameVar(), value);
            }
        }
    }

    private void send(Long sDVConnectorOutputId, List<ComponentModel> modelComponents, Incident incident, Map<String, Object> resultMap) {
        Boolean test = false;
        String url = null;

        if (Objects.nonNull(sDVConnectorOutputId)) {
            url = coreServiceClient.getUrl(sDVConnectorOutputId);

            Optional<ComponentModel> componentModelConnectorOutOptional = modelComponents
                    .stream()
                    .filter(component -> component.getSummaryDataVersionId().equals(sDVConnectorOutputId))
                    .findFirst();

            if (componentModelConnectorOutOptional.isPresent()) {
                ComponentModel connectorOutput = componentModelConnectorOutOptional.get();

                test = connectorOutput.getTest();
                Map<String, Object> validatedFields = ruleEvaluateService.validateFields(connectorOutput.getSummaryFields(), resultMap);
                updateError(sDVConnectorOutputId, validatedFields);
            } else {
                throw new NotFoundException(String.format("Connector output %s not found to send data by url", sDVConnectorOutputId));
            }
        }

        if (Objects.nonNull(test) && test) {
            sendToTestService(incident, qualityRuleDataToProcess.getUserId(), sDVConnectorOutputId);
        } else {
            if (Objects.nonNull(url)) {
                sendToUrl(url, sDVConnectorOutputId, incident.getJsonData());
            }
        }
    }

    private void updateError(Long sDVComponentId, Map<String, Object> validatedFields) {
        List<Object> errors = ObjectMapperUtils.convertValue(validatedFields.get(ERRORS_FIELD), new TypeReference<>() {
        });
        if (!errors.isEmpty()) {
            coreServiceClient.updateError(qualityRuleDataToProcess.getSDVModelId(), sDVComponentId, errors.get(errors.size() - 1).toString());
        }
    }

    private void sendToTestService(Incident i, Long userId, Long sDVConnectorOutputId) {
        IncidentResponseDTO incidentResponseDTO = IncidentResponseDTO
                .builder()
                .number(i.getNumber())
                .textValue(i.getTextValue())
                .sDVConnectorId(i.getSummaryDataVersionConnectorId())
                .sDVModelId(i.getSummaryDataVersionModelId())
                .jsonData(i.getJsonData())
                .userId(userId)
                .sDVConnectorOutputId(sDVConnectorOutputId)
                .build();

        testServiceClient.handleIncident(incidentResponseDTO);
    }

    private void sendToUrl(String url, Long summaryDataVersionId, String jsonData) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonData, headers);

        int statusCode;
        try {
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.info(Strings.repeat("-", 100));
            log.info(String.format("ConnectorOutId %s, result from another service - %s",
                    summaryDataVersionId, res.getBody()));
            log.info(Strings.repeat("-", 100));

            statusCode = res.getStatusCode().value();
        } catch (Exception e) {
            log.error(e.getMessage());
            statusCode = HttpStatus.BAD_REQUEST.value();
        }

        countOutputConnector(summaryDataVersionId, statusCode);
    }

    private void countOutputConnector(Long sDVOutputId, int statusCode) {
        String hashKey;
        String key = sDVOutputId.toString();

        if (statusCode == 200) {
            hashKey = amountOfTransactionRedisPrefix;
        } else {
            hashKey = amountOfTransactionErrorsRedisPrefix;
        }
        hashKey += ":" + qualityRuleDataToProcess.getSDVModelId();

        Boolean isSuccess = redisUtil.increment(hashKey, key, DEFAULT_COUNTER_VALUE);
        if (!isSuccess) {
            coreServiceClient.incrementTransactions(qualityRuleDataToProcess.getSDVModelId(), sDVOutputId);
        }
    }

    private Pair<String, QualityOperationDTO> executeQualityRule(List<RuleExpressionDTO> ruleExpressionDTOS,
                                                                 ConnectorInputEvaluateData connectorInputEvaluateData) {
        Boolean res = false;
        QualityOperationDTO qualityOperationDTOCacheDTO = null;
        String condition = Strings.EMPTY;

        for (RuleExpressionDTO ruleExpressionDTO : ruleExpressionDTOS) {
            if (ruleExpressionDTO.getType().equals(ExpressionType.NO_EXPRESSION)) {
                qualityOperationDTOCacheDTO = ruleExpressionDTO.getQualityOperationDTO();
                continue;
            }

            try {
                res = ruleEvaluateService.evaluateRuleMap(connectorInputEvaluateData, ruleExpressionDTO.getExpression());
            } catch (InvalidRuleException | SpelEvaluationException e) {
                continue;
            }

            if (res) {
                qualityOperationDTOCacheDTO = ruleExpressionDTO.getQualityOperationDTO();
                condition = ruleExpressionDTO.getExpression();
                break;
            }
        }

        if (!res) {
            if (Objects.isNull(qualityOperationDTOCacheDTO)) {
                return getDefault();
            }
            return Pair.of(condition, qualityOperationDTOCacheDTO);
        }

        return Pair.of(condition, qualityOperationDTOCacheDTO);
    }

    private Pair<String, QualityOperationDTO> getDefault() {
        QualityOperationDTO qualityOperationDTO = QualityOperationDTO.builder()
                .toIncidents(true)
                .textValue(Fields.UNDEFINED)
                .number(DEFAULT_NUMBER)
                .build();

        return Pair.of(Strings.EMPTY, qualityOperationDTO);
    }

    private Incident createIncident(
            Long sDVModelId,
            Long sDVConnectorId,
            QualityOperationDTO qualityOperationDTO,
            Long userId,
            List<RuleInfo> ruleInfos) {
        Incident incident = new Incident();
        incident.setSummaryDataVersionModelId(sDVModelId);
        incident.setSummaryDataVersionConnectorId(sDVConnectorId);
        incident.setNumber(qualityOperationDTO.getNumber());
        incident.setUserId(userId);
        incident.setTextValue(qualityOperationDTO.getTextValue());
        incident.setJsonData(ObjectMapperUtils.getJson(ruleInfos));

        return incident;
    }

}
