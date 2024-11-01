package kz.symtech.antifraud.connectorhandlerservice.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import kz.symtech.antifraud.connectorhandlerservice.dto.*;
import kz.symtech.antifraud.connectorhandlerservice.enums.TransactionInputDataType;
import kz.symtech.antifraud.connectorhandlerservice.exceptions.TransactionInputDataException;
import kz.symtech.antifraud.connectorhandlerservice.facades.QualityRuleFacade;
import kz.symtech.antifraud.connectorhandlerservice.facades.QuantityRuleFacade;
import kz.symtech.antifraud.connectorhandlerservice.services.ConnectorInputHandlerService;
import kz.symtech.antifraud.connectorhandlerservice.services.RuleEvaluateService;
import kz.symtech.antifraud.connectorhandlerservice.services.TransactionInputDataService;
import kz.symtech.antifraud.connectorhandlerservice.utils.RedisUtil;
import kz.symtech.antifraud.connectorhandlerservice.utils.TransactionDataUtil;
import kz.symtech.antifraud.feignclients.clients.services.CoreServiceClient;
import kz.symtech.antifraud.models.dto.ElasticSaveRequestDTO;
import kz.symtech.antifraud.models.dto.model.*;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kz.symtech.antifraud.models.enums.Fields.DEFAULT_COUNTER_VALUE;
import static kz.symtech.antifraud.models.enums.Fields.ERRORS_FIELD;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectorInputHandlerServiceImpl implements ConnectorInputHandlerService {

    private static final Object lock = new Object();

    private final RuleEvaluateService ruleEvaluateService;
    private final CoreServiceClient coreServiceClient;
    private final RedisUtil redisUtil;
    private final QualityRuleFacade qualityRuleFacade;
    private final QuantityRuleFacade quantityRuleFacade;
    private final TransactionInputDataService transactionInputDataService;
    private final Map<UUID, ModelStructResponseDTO> modelStructMap = new ConcurrentHashMap<>();

    @Value("${redis-prefixes.counter-requests-core}")
    private String counterRequestsCoreRedisPrefix;

    @Value("${redis-prefixes.transaction-input}")
    private String transactionInputRedisPrefix;

    @Value("${redis-prefixes.amount-of-transactions}")
    private String amountOfTransactionRedisPrefix;

    @Value("${redis-prefixes.amount-of-transaction-errors}")
    private String amountOfTransactionErrorsRedisPrefix;

    @Override
    public Set<String> handleConnectorInput(
            UUID summaryDataIdModel, UUID summaryDataIdConnector, Map<String, Object> transaction, Long userId) {

        countRequests();

        ModelStructResponseDTO modelStructResponseDTO;
        if (modelStructMap.containsKey(summaryDataIdModel)) {
            modelStructResponseDTO = modelStructMap.get(summaryDataIdModel);
        } else {
            modelStructResponseDTO = coreServiceClient.getModelInfo(summaryDataIdModel);
            modelStructMap.put(summaryDataIdModel, modelStructResponseDTO);
        }

        Set<String> resposenSet = new HashSet<>();

        ComponentModel componentConnector = getComponentBySummaryDataIdAndActive(
                modelStructResponseDTO.getComponents(), summaryDataIdConnector);

        Long sDVConnectorId = componentConnector.getSummaryDataVersionId();
        Long sDVModelId = modelStructResponseDTO.getSummaryDataVersionId();

        Map<String, Object> validatedFields = new HashMap<>();
        Boolean isCounterCompleted = countConnector(
                modelStructResponseDTO.getTransactionCounterDTOMap(), sDVModelId, sDVConnectorId, componentConnector, validatedFields, transaction);
        if (isCounterCompleted) {
            return Set.of();
        }

        List<String> transactionInputDataErrors = new ArrayList<>();
        validate(
                componentConnector.getSummaryFields(),
                componentConnector.getSummaryFields()
                        .stream()
                        .filter(x -> x.getParentSummaryField() == null)
                        .toList(),
                transaction,
                transactionInputDataErrors
        );

        if (!componentConnector.getLaunchSecondStage()) {
            log.info(String.format("Component '%s' is not on second stage", sDVConnectorId));
            log.info("Saving transaction to cache...");
            saveToCacheTransactionInput(sDVModelId, sDVConnectorId, transaction);
            return Set.of();
        }

        resposenSet.add(addToElastic(transaction, validatedFields, componentConnector, userId));

        ConnectorInputEvaluateData connectorInputEvaluateDataResult = ConnectorInputEvaluateData
                .builder()
                .map(transaction)
                .build();

        List<RuleInfo> rulesInfo = new ArrayList<>();
        Map<String, Object> rulesErrorsMap;
        List<FieldRelationDTO> fieldRelations = modelStructResponseDTO.getFieldRelations();
        Map<Long, SummaryFieldCacheDTO> summaryFieldMap = getSummaryFieldMap(modelStructResponseDTO);
        Map<Long, ModelRuleCacheDTO> ruleMap = modelStructResponseDTO.getRuleMap();

        for (Map.Entry<Long, ModelRuleCacheDTO> entry : ruleMap.entrySet()) {
            ModelRuleCacheDTO rule = entry.getValue();
            Long ruleSDVId = entry.getKey();

            List<FieldRelationDTO> ruleFieldRelations = fieldRelations.stream()
                    .filter(fieldRelation -> fieldRelation.getSummaryDataVersionId().equals(ruleSDVId))
                    .filter(fieldRelation -> rule.getSummaryFieldRuleIds().contains(fieldRelation.getSummaryFieldId()))
                    .toList();

            Set<SummaryFieldCacheDTO> summaryFields = new HashSet<>();
            TransactionDataUtil.fillSummaryFields(summaryFields, summaryFieldMap, ruleFieldRelations, true);

            ConnectorInputEvaluateData connectorInputEvaluateData = build(
                    ruleFieldRelations,
                    transaction,
                    sDVModelId,
                    sDVConnectorId,
                    summaryFields.stream().toList());
            log.info("ConnectorInputEvaluateData after build: " + connectorInputEvaluateData);

            List<String> incompleteDataErrors = checkIfMapIsReadyToProcess(connectorInputEvaluateData.getMap(), ruleFieldRelations);
            if (!incompleteDataErrors.isEmpty()) {
                log.error(String.format("Incomplete data %s", incompleteDataErrors));
                saveToCacheTransactionInput(sDVModelId, sDVConnectorId, transaction);
                return Set.of();
            }
            rulesErrorsMap = new HashMap<>();

            switch (rule.getType()) {
                case QUANTITY -> {
                    synchronized (lock) {
                        QuantityRuleDataToProcess quantityRuleDataToProcess = QuantityRuleDataToProcess
                                .builder()
                                .sDVRuleId(ruleSDVId)
                                .connectorInputEvaluateData(connectorInputEvaluateData)
                                .connectorInputEvaluateDataResult(connectorInputEvaluateDataResult)
                                .rulesErrorsMap(rulesErrorsMap)
                                .ruleResults(rulesInfo)
                                .ruleExpressionDTOS(rule.getRuleExpressionDTOS())
                                .fieldRelationDTOS(ruleFieldRelations)
                                .build();

                        quantityRuleFacade.setQuantityRuleDataToProcess(quantityRuleDataToProcess);
                        quantityRuleFacade.executeQuantityRule();
                    }
                }
                case QUALITY -> {
                    synchronized (lock) {
                        QualityRuleDataToProcess qualityRuleDataToProcess = QualityRuleDataToProcess
                                .builder()
                                .connectorInputEvaluateData(connectorInputEvaluateData)
                                .connectorInputEvaluateDataResult(connectorInputEvaluateDataResult)
                                .componentModelConnector(componentConnector)
                                .sDVModelId(sDVModelId)
                                .userId(userId)
                                .modelStructComponents(modelStructResponseDTO.getComponents())
                                .fieldRelationCacheDTOList(fieldRelations)
                                .ruleResults(rulesInfo)
                                .transaction(transaction)
                                .ruleExpressionDTOS(rule.getRuleExpressionDTOS())
                                .summaryFieldMap(summaryFieldMap)
                                .build();

                        qualityRuleFacade.setQualityRuleDataToProcess(qualityRuleDataToProcess);
                        qualityRuleFacade.handleQualityRule();
                    }
                }
                default -> throw new IllegalArgumentException(String.format("Unknown rule type: %s", rule.getType()));
            }

            if (!rulesErrorsMap.isEmpty()) {
                coreServiceClient.updateError(sDVModelId, ruleSDVId, rulesErrorsMap.get(Fields.RULES_FIELD).toString());
            }

            countRule(modelStructResponseDTO.getTransactionCounterDTOMap(), sDVModelId, ruleSDVId, rulesErrorsMap, connectorInputEvaluateDataResult);
            saveToCacheTransactionInputRule(sDVModelId, sDVConnectorId, connectorInputEvaluateDataResult.getMap(), rule.getSummaryDataVersionId());

            ComponentModel ruleComponent = getRuleComponent(modelStructResponseDTO.getComponents(), ruleSDVId);

            Map<String, Object> data = new HashMap<>();
            data.putAll(connectorInputEvaluateDataResult.getMap());
            data.putAll(validatedFields);
            data.put(Fields.EXPIRES_AT_FIELD, getExpiresAtDate(new Date(), ruleComponent.getDaysRemaining()));
            data.put(Fields.RESULT_FIELD, connectorInputEvaluateDataResult.getMap().get(Fields.RESULT_FIELD).toString());
            data.put(Fields.CONNECTOR_INPUT_ID_FIELD, componentConnector.getSummaryDataVersionId());
            data.putAll(rulesErrorsMap);
            data.put(Fields.RULE_ID_FIELD, rule.getSummaryDataVersionId());
            data.put(Fields.CREATED_AT_FIELD, new Date());

            Boolean saveResult = ruleComponent.getDaysRemaining() > 0;
            Boolean increment = ruleComponent.getResultIncremental();
            String jsonData = ObjectMapperUtils.getJson(data);
            UUID ruleUUID = new UUID(rule.getSummaryDataVersionId(), 0x1L);

            ElasticSaveRequestDTO elasticSaveRequestDTO = buildElasticRequestDTO(saveResult, increment, userId, ruleUUID, jsonData);
            String result = coreServiceClient.saveDocument(elasticSaveRequestDTO);
            resposenSet.add(result);
        }

        saveToCacheTransactionInput(sDVModelId, sDVConnectorId, transaction);

        return resposenSet;
    }

    @Override
    public void changeState(UUID summaryDataIdModel, TransactionCounterState transactionCounterState) {
        log.info("Changing states for key {}, state - {}", summaryDataIdModel, transactionCounterState);
        if (modelStructMap.containsKey(summaryDataIdModel)) {
            ModelStructResponseDTO model = modelStructMap.get(summaryDataIdModel);

            model.getTransactionCounterDTOMap()
                    .values()
                    .forEach(transactionCounter -> transactionCounter.setState(transactionCounterState.name()));

            modelStructMap.put(summaryDataIdModel, model);
        }
    }

    @Override
    public void changeModel(UUID summaryDataIdModel, ModelStructResponseDTO modelStructResponseDTO) {
        modelStructMap.put(summaryDataIdModel, modelStructResponseDTO);
    }

    private Boolean countConnector(Map<Long, TransactionCounterDTO> transactionCounterDTOMap,
                                   Long sDVModelId,
                                   Long sDVConnectorId,
                                   ComponentModel componentConnector,
                                   Map<String, Object> validatedFields,
                                   Map<String, Object> transaction) {

        TransactionCounterDTO transactionCounterDTOConnector = Optional.ofNullable(transactionCounterDTOMap.get(sDVConnectorId))
                .orElseThrow(() -> new NotFoundException(String.format("TransactionCounter not found with id '%s'", sDVConnectorId)));

        String modelConnectorInputState = transactionCounterDTOConnector.getState();
        if (modelConnectorInputState.equals(TransactionCounterState.STOP.name()) ||
                modelConnectorInputState.equals(TransactionCounterState.PAUSE.name())) {
            log.info("Transaction with (s.d.v) model id {} and (s.d.v) connector id {} on {} state",
                    sDVModelId, sDVConnectorId, modelConnectorInputState);
            return true;
        }

        if (componentConnector.getValidateFields()) {
            validatedFields.putAll(validateSummaryFields(componentConnector, transaction));
            incrementAmountErrors(sDVModelId, sDVConnectorId, Long.parseLong(validatedFields.get(Fields.ERRORS_COUNT).toString()));

            List<Object> errors = ObjectMapperUtils.convertValue(validatedFields.get(ERRORS_FIELD), new TypeReference<>() {
            });
            if (!errors.isEmpty()) {
                coreServiceClient.updateError(sDVModelId, sDVConnectorId, errors.get(errors.size() - 1).toString());
            }
        }

        incrementAmountTransactions(sDVModelId, sDVConnectorId);

        return false;
    }

    private void incrementAmountErrors(Long sDVModelId, Long sDVComponentId, Long delta) {
        String hashKey = amountOfTransactionErrorsRedisPrefix + ":" + sDVModelId;
        String key = sDVComponentId.toString();

        Boolean isSuccess = redisUtil.increment(hashKey, key, delta);
        if (!isSuccess) {
            coreServiceClient.incrementErrors(sDVModelId, sDVComponentId, delta.intValue());
        }
    }

    private void incrementAmountTransactions(Long sDVModelId, Long sDVComponentId) {
        String hashKey = amountOfTransactionRedisPrefix + ":" + sDVModelId;
        String key = sDVComponentId.toString();

        Boolean isSuccess = redisUtil.increment(hashKey, key, DEFAULT_COUNTER_VALUE);
        if (!isSuccess) {
            coreServiceClient.incrementTransactions(sDVModelId, sDVComponentId);
        }
    }

    private ComponentModel getComponentBySummaryDataIdAndActive(List<ComponentModel> componentModels, UUID summaryDataId) {
        return componentModels
                .stream()
                .filter(componentModel -> componentModel.getSummaryDataId().equals(summaryDataId) && componentModel.getIsActive())
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("summary data with id '%s' is not found or not active", summaryDataId)));
    }

    private ElasticSaveRequestDTO buildElasticRequestDTO(Boolean saveResult, Boolean increment, Long userId, UUID id, String json) {
        return ElasticSaveRequestDTO
                .builder()
                .saveResult(saveResult)
                .increment(increment)
                .userId(userId)
                .id(id)
                .jsonData(json)
                .build();
    }

    private Map<Long, SummaryFieldCacheDTO> getSummaryFieldMap(ModelStructResponseDTO modelStructResponseDTO) {
        List<SummaryFieldCacheDTO> summaryFields = new ArrayList<>();
        modelStructResponseDTO.getComponents().forEach(component -> summaryFields.addAll(component.getSummaryFields()));

        return summaryFields.stream()
                .collect(Collectors.toMap(SummaryFieldCacheDTO::getId, Function.identity()));
    }

    private void countRequests() {
        redisUtil.increment(counterRequestsCoreRedisPrefix);
    }

    private void validate(
            List<SummaryFieldCacheDTO> summaryFieldList,
            List<SummaryFieldCacheDTO> summaryFieldChildrenList,
            Map<String, Object> transaction,
            List<String> errors) {

        if (transaction.size() != summaryFieldChildrenList.size()) {
            List<String> names = summaryFieldChildrenList.stream().map(SummaryFieldCacheDTO::getName).toList();

            for (Map.Entry<String, Object> entry : transaction.entrySet()) {
                if (!names.contains(entry.getKey())) {
                    String parentsHierarchy = getHierarchyOfParents(new StringBuilder(), summaryFieldChildrenList.get(0));

                    String error = parentsHierarchy.isEmpty()
                            ? String.format("Invalid key '%s'", entry.getKey())
                            : String.format("Invalid key '%s' with parents '%s'", entry.getKey(), parentsHierarchy);
                    errors.add(error);
                }
            }
        }

        summaryFieldChildrenList.forEach(summaryField -> {
            if (summaryField.getType().equals(SummaryFieldType.OBJECT)) {
                List<SummaryFieldCacheDTO> children = summaryFieldList
                        .stream()
                        .filter(x -> x.getParentSummaryField() != null)
                        .filter(x -> x.getParentSummaryField().getId().equals(summaryField.getId()))
                        .toList();
                Map<String, Object> childTransaction = ObjectMapperUtils.fromObjectToMap(transaction.get(summaryField.getName()));
                validate(summaryFieldList, children, childTransaction, errors);
            } else {
                if (transaction.containsKey(summaryField.getName())) {
                    Object value = transaction.get(summaryField.getName());

                    if (!checkType(value, summaryField.getType().name())) {
                        errors.add(String.format("Type '%s' of field '%s' is incorrect, must be '%s'",
                                value.getClass().getSimpleName(), summaryField.getName(), summaryField.getType()));
                    }
                } else {
                    errors.add(String.format("There is no field with name '%s'", summaryField.getName()));
                }
            }
        });

        if (!errors.isEmpty()) {
            throw new TransactionInputDataException(errors);
        }
    }

    private String getHierarchyOfParents(StringBuilder result, SummaryFieldCacheDTO summaryField) {
        if (Objects.nonNull(summaryField.getParentSummaryField())) {
            if (result.length() > 0) {
                result.append(" -> ");
            }
            result.append(summaryField.getParentSummaryField().getName());

            getHierarchyOfParents(result, summaryField.getParentSummaryField());
        }
        return result.toString();
    }

    private Boolean checkType(Object value, String expectedType) {
        return value.getClass().getSimpleName().equalsIgnoreCase(expectedType);
    }


    private void saveToCacheTransactionInput(
            Long sDVModelId, Long sDVConnectorId, Map<String, Object> data) {

        String key = sDVModelId + ":" + sDVConnectorId;

        TransactionInputDataDTO transactionInputDataDTO = new TransactionInputDataDTO();
        transactionInputDataDTO.setSummaryDataVersionModelId(sDVModelId);
        transactionInputDataDTO.setSummaryDataVersionConnectorId(sDVConnectorId);
        transactionInputDataDTO.setType(TransactionInputDataType.CONNECTOR);
        transactionInputDataDTO.setData(data);

        Boolean isSuccess = redisUtil.putValue(
                transactionInputRedisPrefix + ":" + TransactionInputDataType.CONNECTOR, key, transactionInputDataDTO);
        if (!isSuccess) {
            transactionInputDataService.save(transactionInputDataDTO);
        }
    }

    private String addToElastic(
            Map<String, Object> transaction, Map<String, Object> validatedFields, ComponentModel componentConnector, Long userId) {

        UUID connectorUUID = new UUID(componentConnector.getSummaryDataVersionId(), 0x1L);
        String jsonDataConnector = ObjectMapperUtils.getJson(new HashMap<>() {{
            putAll(transaction);
            putAll(validatedFields);
            put(Fields.EXPIRES_AT_FIELD, getExpiresAtDate(new Date(), componentConnector.getDaysRemaining()));
        }});
        Boolean saveResultConnector = componentConnector.getDaysRemaining() > 0;

        ElasticSaveRequestDTO elasticSaveRequestDTO = buildElasticRequestDTO(
                saveResultConnector, componentConnector.getResultIncremental(), userId, connectorUUID, jsonDataConnector);

        return coreServiceClient.saveDocument(elasticSaveRequestDTO);
    }

    private Map<String, Object> validateSummaryFields(ComponentModel connectorInput, Map<String, Object> t) {
        List<SummaryFieldCacheDTO> summaryFields = connectorInput.getSummaryFields()
                .stream()
                .filter(s -> Objects.nonNull(s.getSummaryFieldSubDataDTO()))
                .toList();

        return ruleEvaluateService.validateFields(summaryFields, t);
    }

    private Date getExpiresAtDate(Date createdAt, int daysRemaining) {
        return new Date(createdAt.getTime() + Duration.ofDays(daysRemaining).toMillis());
    }

    private ConnectorInputEvaluateData build(
            List<FieldRelationDTO> fieldRelations,
            Map<String, Object> transaction,
            Long summaryDataVersionModelId,
            Long sDVModelConnectorInputId,
            List<SummaryFieldCacheDTO> summaryFieldCacheDTOList) {

        Map<String, Object> currentMap = new HashMap<>();
        Map<String, Object> prevMap = new HashMap<>();

        fieldRelations.forEach(fieldRelation -> {
            if (fieldRelation.getDefaultField()) {
                // processing the "result" field
                processDefaultField(summaryDataVersionModelId, sDVModelConnectorInputId, fieldRelation, currentMap);
            } else if (Objects.equals(fieldRelation.getSDVSourceId(), sDVModelConnectorInputId)) {
                // building data from current transaction
                TransactionDataUtil.fillMapWithTransactionData(
                        transaction, summaryFieldCacheDTOList, fieldRelation, currentMap);
                // building data from previous transaction
                processPrevTransactionOrAnotherConnector(
                        summaryDataVersionModelId, sDVModelConnectorInputId, prevMap, fieldRelation, summaryFieldCacheDTOList);
            } else {
                // building data from another connector
                processPrevTransactionOrAnotherConnector(
                        summaryDataVersionModelId, fieldRelation.getSDVSourceId(), currentMap, fieldRelation, summaryFieldCacheDTOList);
            }
        });

        log.info(Strings.repeat("-", 100));
        log.info("prev - " + prevMap);
        log.info("current - " + currentMap);
        log.info(Strings.repeat("-", 100));

        return ConnectorInputEvaluateData.builder()
                .prevMap(prevMap)
                .map(currentMap)
                .build();
    }

    private void processDefaultField(
            Long summaryDataVersionModelId,
            Long sDVModelConnectorInputId,
            FieldRelationDTO fieldRelationCacheDTO,
            Map<String, Object> currentMap) {

        String key = summaryDataVersionModelId + ":" + sDVModelConnectorInputId + ":" + fieldRelationCacheDTO.getSDVSourceId();

        TransactionInputDataDTORule transactionInputDataRule = (TransactionInputDataDTORule)
                redisUtil.getValue(transactionInputRedisPrefix + ":" + TransactionInputDataType.RULE, key);
        if (Objects.isNull(transactionInputDataRule)) {
            transactionInputDataRule = transactionInputDataService
                    .get(summaryDataVersionModelId, sDVModelConnectorInputId, fieldRelationCacheDTO.getSDVSourceId());
        }
        currentMap.put(fieldRelationCacheDTO.getFieldNameVar(), transactionInputDataRule.getData().get(Fields.RESULT_FIELD));
    }

    private void processPrevTransactionOrAnotherConnector(
            Long summaryDataVersionModelId,
            Long sDVModelConnectorInputId,
            Map<String, Object> map,
            FieldRelationDTO fieldRelationCacheDTO,
            List<SummaryFieldCacheDTO> summaryFieldCacheDTOList) {

        String key = summaryDataVersionModelId + ":" + sDVModelConnectorInputId;
        TransactionInputDataDTO transactionInputDataDTO = (TransactionInputDataDTO)
                redisUtil.getValue(transactionInputRedisPrefix + ":" + TransactionInputDataType.CONNECTOR, key);

        if (Objects.isNull(transactionInputDataDTO)) {
            transactionInputDataDTO = transactionInputDataService.get(summaryDataVersionModelId, sDVModelConnectorInputId, null);
            if (Objects.isNull(transactionInputDataDTO)) {
                return;
            }
        }

        Map<String, Object> transaction = transactionInputDataDTO.getData();

        TransactionDataUtil.fillMapWithTransactionData(
                transaction, summaryFieldCacheDTOList, fieldRelationCacheDTO, map);
    }

    private List<String> checkIfMapIsReadyToProcess(Map<String, Object> map, List<FieldRelationDTO> fieldRelationCacheDTOList) {
        List<String> errors = new ArrayList<>();

        fieldRelationCacheDTOList.forEach(fieldRelationCacheDTO -> {
            if (!map.containsKey(fieldRelationCacheDTO.getFieldNameVar())) {
                errors.add(String.format("There is no field with name '%s', (s.d.v) connector id '%s'",
                        fieldRelationCacheDTO.getFieldNameVar(), fieldRelationCacheDTO.getSDVSourceId()));
            }
        });

        return errors;
    }

    private void countRule(
            Map<Long, TransactionCounterDTO> transactionCounterDTOMap,
            Long sDVModelId,
            Long ruleSummaryDataVersionId,
            Map<String, Object> rulesErrorsMap,
            ConnectorInputEvaluateData connectorInputEvaluateDataResult) {

        TransactionCounterDTO transactionCounterDtoRule = Optional.ofNullable(transactionCounterDTOMap.get(ruleSummaryDataVersionId))
                .orElseThrow(() -> new NotFoundException(String.format("TransactionCounter not found with id '%s'", ruleSummaryDataVersionId)));

        if (transactionCounterDtoRule.getState().equals(TransactionCounterState.RUN.name())) {
            Object resultValue = connectorInputEvaluateDataResult.getMap().get(Fields.RESULT_FIELD);

            if (rulesErrorsMap.containsKey(Fields.RULES_COUNT) || resultValue.equals(Fields.UNDEFINED)) {
                incrementAmountErrors(sDVModelId, ruleSummaryDataVersionId, DEFAULT_COUNTER_VALUE);
            }

            incrementAmountTransactions(sDVModelId, ruleSummaryDataVersionId);
        }
    }

    private void saveToCacheTransactionInputRule(
            Long sDVModelId, Long sDVConnectorId, Map<String, Object> data, Long sDVRuleId) {

        String key = sDVModelId + ":" + sDVConnectorId + ":" + sDVRuleId;

        TransactionInputDataDTORule transactionInputDataRule = new TransactionInputDataDTORule();
        transactionInputDataRule.setSummaryDataVersionModelId(sDVModelId);
        transactionInputDataRule.setSummaryDataVersionConnectorId(sDVConnectorId);
        transactionInputDataRule.setType(TransactionInputDataType.RULE);
        transactionInputDataRule.setData(data);
        transactionInputDataRule.setSummaryDataVersionRuleId(sDVRuleId);

        Boolean isSuccess = redisUtil.putValue(
                transactionInputRedisPrefix + ":" + TransactionInputDataType.RULE, key, transactionInputDataRule);
        if (!isSuccess) {
            transactionInputDataService.save(transactionInputDataRule);
        }
    }

    private ComponentModel getRuleComponent(List<ComponentModel> modelStructComponents, Long sDVRuleId) {
        return modelStructComponents.stream()
                .filter(m -> m.getSummaryDataVersionId().equals(sDVRuleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Model Rule Component '%s' not found", sDVRuleId)));
    }
}
