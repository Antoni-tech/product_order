package kz.symtech.antifraud.coreservice.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import kz.symtech.antifraud.coreservice.dto.*;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryDataResponseDTO;
import kz.symtech.antifraud.coreservice.dto.ModelConnectorDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelConnectorInputGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelConnectorOutputGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelRuleGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelStructGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.model.struct.request.*;
import kz.symtech.antifraud.coreservice.entities.models.*;
import kz.symtech.antifraud.coreservice.entities.rules.QualityOperation;
import kz.symtech.antifraud.coreservice.entities.rules.QualityRuleField;
import kz.symtech.antifraud.coreservice.entities.rules.QuantityRule;
import kz.symtech.antifraud.coreservice.entities.rules.RuleExpression;
import kz.symtech.antifraud.coreservice.entities.summary.*;
import kz.symtech.antifraud.coreservice.enums.ExtendedType;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import kz.symtech.antifraud.coreservice.exceptions.ApplicationException;
import kz.symtech.antifraud.coreservice.exceptions.SummaryDataVersionNotFoundException;
import kz.symtech.antifraud.coreservice.mapper.*;
import kz.symtech.antifraud.coreservice.repository.*;
import kz.symtech.antifraud.coreservice.services.ConnectorUtilService;
import kz.symtech.antifraud.coreservice.services.ModelService;
import kz.symtech.antifraud.coreservice.services.SummaryFieldService;
import kz.symtech.antifraud.coreservice.services.ValidatingService;
import kz.symtech.antifraud.coreservice.utils.RedisUtil;
import kz.symtech.antifraud.feignclients.clients.services.AdminServiceClient;
import kz.symtech.antifraud.models.dto.UserListDTO;
import kz.symtech.antifraud.models.dto.UserResponseDTO;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.ModelRuleCacheDTO;
import kz.symtech.antifraud.models.enums.ExpressionType;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import kz.symtech.antifraud.models.enums.SummarySubType;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private static final String EMAIL = "symtech@symtech.kz";
    private static final short DEFAULT_VERSION = 1;
    private final Long userId = 1L;
    private final ModelConnectorInputRepository modelConnectorInputRepository;
    private final ModelConnectorOutputRepository modelConnectorOutputRepository;
    private final ModelRuleRepository modelRuleRepository;
    private final ModelStructRepository modelStructRepository;
    private final SummaryDataVersionRepository summaryDataVersionRepository;
    private final SummaryDataRepository summaryDataRepository;
    private final ModelStructComponentsRepository modelStructComponentsRepository;
    private final ModelComponentElementsRepository modelComponentElementsRepository;
    private final ConnectorUtilService connectorUtilService;
    private final ValidatingService validatingService;
    private final EntityManager entityManager;
    private final SummaryDataDTOMapper summaryDataDTOMapper;
    private final ModelConnectorOutputDTOMapper modelConnectorOutputDTOMapper;
    private final ModelRuleDTOMapper modelRuleDTOMapper;
    private final GetResponseDTOMapper getResponseDTOMapper;
    private final ModelRuleCacheMapper modelRuleCacheMapper;
    private final ModelComponentCacheMapper modelComponentCacheMapper;
    private final RedisUtil redisUtil;
    private final SummaryFieldService summaryFieldService;
    private final RuleExpressionRepository ruleExpressionRepository;
    private final DataStructureDTOMapper dataStructureDTOMapper;
    private final AdminServiceClient adminServiceClient;
    private final SummaryFieldDTOMapper summaryFieldDTOMapper;

    @Value("${redis-prefixes.model-rule}")
    private String modelRuleRedisPrefix;

    public SummaryDataVersion createSummaryDataVersion(Map<String, Object> map, SummaryDataType type) {
        SummaryData summaryData;
        SummaryDataVersion summaryDataVersion = new SummaryDataVersion();

        if (Boolean.TRUE.equals(map.get(Fields.IS_CREATE_FIELD))) {
            summaryData = createSummaryData(type);
            summaryDataVersion.setIsActive(true);
        } else {
            long id = Long.parseLong((String) map.get(Fields.VERSION_ID_KEY));
            summaryDataVersion = summaryDataVersionRepository.findById(id)
                    .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));

            summaryData = summaryDataVersion.getSummaryData();

            if (summaryDataVersion.getState().equals(SummaryVersionState.PUBLISHED)) {
                summaryDataVersion = new SummaryDataVersion();
                map.put(Fields.IS_CREATE_FIELD, true);
                summaryDataVersion.setIsActive(false);
            }
        }

        return getSummaryDataVersion(map, summaryData, summaryDataVersion);
    }

    private SummaryData createSummaryData(SummaryDataType type) {
        SummaryData summaryData = new SummaryData();
        summaryData.setUserCreateId(userId);
        summaryData.setEmail(EMAIL);
        summaryData.setVersion(DEFAULT_VERSION);
        summaryData.setType(type);
        summaryDataRepository.save(summaryData);
        return summaryData;
    }

    private SummaryDataVersion getSummaryDataVersion(Map<String, Object> map, SummaryData summaryData, SummaryDataVersion summaryDataVersion) {
        summaryDataVersion.setSummaryData(summaryData);
        if (!summaryDataVersion.getSummaryData().getType().equals(SummaryDataType.MODEL)
                && Objects.nonNull(map.get(Fields.EXTEND_FIELD))) {
            validateExtended(map, summaryDataVersion);
        }

        connectorUtilService.getDeclaredFields(Objects.requireNonNull(summaryDataVersion), map);
        summaryDataVersion.setUserUpdateId(userId);
        summaryDataVersion.setVersion(summaryData.getVersion());
        summaryDataVersion.setState(SummaryVersionState.DRAFT);
        summaryDataVersion.setIsTemplate(false);
        summaryDataVersion.setValidateFields(false);

        summaryDataVersionRepository.save(summaryDataVersion);
        return summaryDataVersion;
    }

    private void validateExtended(Map<String, Object> map, SummaryDataVersion summaryDataVersion) {
        Map<String, Object> extended = ObjectMapperUtils.fromObjectToMap(map.get(Fields.EXTEND_FIELD));
        String extendedId = (String) extended.get(Fields.ID_FIELD);
        String extendedType = (String) extended.get(Fields.TYPE_FIELD);

        if (!StringUtils.isNullOrEmpty(extendedId)) {
            long idLong = Long.parseLong(extendedId);
            SummaryDataVersion parentSummaryDataVersion = summaryDataVersionRepository.findById(idLong)
                    .orElseThrow(() -> new SummaryDataVersionNotFoundException(idLong));

            if (parentSummaryDataVersion.getId().equals(summaryDataVersion.getExtended())) {
                List<SummaryField> summaryFields = summaryFieldService.duplicateFields(parentSummaryDataVersion, summaryDataVersion);

                if (ExtendedType.valueOf(extendedType).equals(ExtendedType.REPLACE)
                        && Boolean.FALSE.equals(map.get(Fields.IS_CREATE_FIELD))) {
                    summaryFieldService.checkForFieldRelation(summaryDataVersion.getId(), summaryDataVersion.getSummaryFields());
                    summaryFieldService.deleteSummaryFields(summaryDataVersion.getSummaryFields());
                }
                summaryDataVersion.setSummaryFields(summaryFields);

                boolean dataStructure = parentSummaryDataVersion.getSummaryData().getType().equals(SummaryDataType.DATA_STRUCTURE);
                boolean sameType = parentSummaryDataVersion.getSummaryData().getType().equals(summaryDataVersion.getSummaryData().getType());
                boolean notExtended = Objects.isNull(parentSummaryDataVersion.getExtended());

                if (dataStructure || sameType) {
                    if (notExtended) {
                        summaryDataVersion.setExtended(idLong);
                    }
                } else {
                    summaryDataVersion.setExtended(null);
                }

                ExtendedRequestDTO extendedRequestDTO = ExtendedRequestDTO.builder()
                        .id(String.valueOf(summaryDataVersion.getExtended()))
                        .type(ExtendedType.valueOf(extendedType))
                        .build();
                map.put(Fields.EXTEND_FIELD, extendedRequestDTO);
                map.put(Fields.EXTENDED_FIELD, summaryDataVersion.getExtended());
            }
        }
    }

    @Override
    @Transactional
    public Long createConnector(Object object) {
        Map<String, Object> map = ObjectMapperUtils.fromObjectToMap(object);
        SummaryDataType type = SummaryDataType.valueOf((String) map.get(Fields.TYPE_FIELD));
        SummaryDataVersion summaryDataVersion = createSummaryDataVersion(map, type);

        Object connectorObj = createEntityWithReflection(map, type, summaryDataVersion);

        if (type.equals(SummaryDataType.CONNECTOR_INPUT)) {
            modelConnectorInputRepository.save((ModelConnectorInput) connectorObj);
        } else {
            modelConnectorOutputRepository.save((ModelConnectorOutput) connectorObj);
        }

        return summaryDataVersion.getId();
    }

    private Object createEntityWithReflection(Map<String, Object> map,
                                              SummaryDataType type,
                                              SummaryDataVersion summaryDataVersion) {
        Class<?> typeAClass = type.getAClass();
        Object modelEntity;

        if (Boolean.TRUE.equals(map.get(Fields.IS_CREATE_FIELD))) {
            modelEntity = createEntityConstructor(type);
        } else {
            modelEntity = connectorUtilService.findBySummaryDataVersionIdWithReflection(type, summaryDataVersion.getId());
        }

        connectorUtilService.getDeclaredFields(modelEntity, map);
        Object extended = map.get(Fields.EXTEND_FIELD);
        if (Objects.isNull(extended)) {
            summaryDataVersion.setSummaryFields(summaryFieldService.createFields(summaryDataVersion, map));
        }

        setSummaryDataVersion(typeAClass, modelEntity, summaryDataVersion);

        return modelEntity;
    }

    private static void setSummaryDataVersion(Class<?> typeAClass, Object modelEntity, SummaryDataVersion summaryDataVersion) {
        try {
            Method setSummaryDataVersionIdMethod = typeAClass.getMethod(Fields.SET_SUMMARY_DATA_VERSION_FIELD, SummaryDataVersion.class);
            setSummaryDataVersionIdMethod.invoke(modelEntity, summaryDataVersion);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createEntityConstructor(SummaryDataType type) {
        try {
            Constructor<T> constructor = (Constructor<T>) type.getAClass().getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Error creating new entity", e);
        }
    }

    @Override
    public Long createDataStructure(Object object) {
        Map<String, Object> objectMap = ObjectMapperUtils.fromObjectToMap(object);
        SummaryDataVersion summaryDataVersion = createSummaryDataVersion(objectMap, SummaryDataType.DATA_STRUCTURE);
        summaryFieldService.createFields(summaryDataVersion, objectMap);
        return summaryDataVersion.getId();
    }

    @Override
    public List<DataStructureDTO> getDataStructures() {
        return summaryDataVersionRepository.findAll()
                .stream()
                .map(dataStructureDTOMapper::buildDataStructureDTOMapperDTO)
                .toList();
    }

    @Override
    public SummaryDataResponseDTO getDataStructure(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));
        SummaryDataResponseDTO summaryDataResponseDTO = summaryDataDTOMapper.buildVersion(summaryDataVersion, true);

        summaryDataResponseDTO.setUserName(getResponseDTOMapper.getName(summaryDataVersion));

        return summaryDataResponseDTO;
    }

    @Override
    public Long duplicateDataStructure(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));

        summaryDataVersion.getSummaryData().setType(SummaryDataType.DATA_STRUCTURE);
        SummaryDataVersion duplicateSummaryDataVersion = duplicateSummaryData(null, summaryDataVersion);
        summaryFieldService.duplicateFields(summaryDataVersion, duplicateSummaryDataVersion);

        return duplicateSummaryDataVersion.getId();
    }

    @Override
    @Transactional
    public Long createModelRule(ModelRuleCreateRequestDTO modelRuleCreateRequestDTO) {
        Map<String, Object> map = ObjectMapperUtils.fromObjectToMap(modelRuleCreateRequestDTO);
        SummaryDataVersion summaryDataVersion = createSummaryDataVersion(map, SummaryDataType.RULE);

        ModelRule modelRule;
        boolean update = false;
        if (Boolean.TRUE.equals(map.get(Fields.IS_CREATE_FIELD))) {
            modelRule = new ModelRule();
        } else {
            update = true;
            modelRule = modelRuleRepository.findBySummaryDataVersionId(summaryDataVersion.getId())
                    .orElseThrow(() -> new SummaryDataVersionNotFoundException(summaryDataVersion.getId()));

        }
        modelRule.setSummarySubType(SummarySubType.valueOf(modelRuleCreateRequestDTO.getSummarySubType()));
        modelRuleCreateRequestDTO.getJsonData().forEach(condition -> validatingService.validate(condition.get("condition").asText()));

        Object extended = map.get(Fields.EXTEND_FIELD);
        if (Objects.isNull(extended)) {
            summaryDataVersion.setSummaryFields(summaryFieldService.createFields(summaryDataVersion, map));
        }
        modelRule.setSummaryDataVersion(summaryDataVersion);

        modelRuleRepository.save(modelRule);
        saveRuleExpression(modelRuleCreateRequestDTO, modelRule.getSummaryDataVersion(), update);

        return summaryDataVersion.getId();
    }

    @Transactional
    public void saveRuleExpression(ModelRuleCreateRequestDTO ruleRequest, SummaryDataVersion summaryDataVersionRule, Boolean update) {
        JsonNode jsonNode = ruleRequest.getJsonData();

        switch (SummarySubType.valueOf(ruleRequest.getSummarySubType())) {
            case QUANTITY -> {
                ruleExpressionRepository.deleteAllBySummaryDataVersionRuleId(summaryDataVersionRule.getId());
                QuantityRule quantityRule = ObjectMapperUtils.treeToValue(jsonNode.get(0), QuantityRule.class);

                RuleExpression conditionRuleExpression =
                        getRuleExpression(quantityRule.getCondition(), ExpressionType.CONDITION, summaryDataVersionRule, null);
                RuleExpression resConditionExpression =
                        getRuleExpression(quantityRule.getResultCondition(), ExpressionType.OPERATION, summaryDataVersionRule, null);

                List<RuleExpression> ruleExpressions = ruleExpressionRepository.saveAll(List.of(conditionRuleExpression, resConditionExpression));
                summaryDataVersionRule.setRuleExpressions(ruleExpressions);
            }
            case QUALITY -> {
                List<QualityRuleField> qualityRuleFields = ObjectMapperUtils.readValue(jsonNode, new TypeReference<>() {
                });

                deleteAllRuleExpressionsByRuleIdAndExpressionIdNotIn(summaryDataVersionRule.getId(), qualityRuleFields);

                List<RuleExpression> ruleExpressions = new ArrayList<>();

                qualityRuleFields.forEach(field -> {
                    RuleExpression ruleExpression =
                            getRuleExpression(field.getCondition(), ExpressionType.CONDITION, summaryDataVersionRule, field.getId());

                    QualityOperation qualityOperation = new QualityOperation();
                    if (Objects.nonNull(ruleExpression.getQualityOperation())) {
                        qualityOperation = ruleExpression.getQualityOperation();
                    }

                    qualityOperation.setNumber(field.getNumber());
                    qualityOperation.setTextValue(field.getTextValue());
                    qualityOperation.setRuleExpression(ruleExpression);
//                    Boolean toIncidents = field.getToIncidents();
//                    qualityOperation.setToIncidents(Objects.nonNull(toIncidents) ? toIncidents : false);

//                    if (Objects.nonNull(field.getConnectorOutputVersionId())) {
//                        SummaryDataVersion summaryDataVersionConnector =
//                                summaryDataVersionRepository.findById(field.getConnectorOutputVersionId())
//                                        .orElseThrow(() -> new NotFoundException("Connector output not found"));
//
//                        if (!summaryDataVersionConnector.getSummaryData().getType().equals(SummaryDataType.CONNECTOR_OUTPUT)) {
//                            throw new IncorrectFieldTypeException(
//                                    String.format("SummaryDataVersion with id '%s' is not CONNECTOR_OUTPUT",
//                                            field.getConnectorOutputVersionId()));
//                        }
//                        qualityOperation.setSummaryDataVersionConnectorOutput(summaryDataVersionConnector);
//                    }

                    ruleExpression.setQualityOperation(qualityOperation);

                    ruleExpressions.add(ruleExpression);
                });

                if (!update) {
                    ruleExpressions.add(getDefault(summaryDataVersionRule));
                }

                ruleExpressionRepository.saveAll(ruleExpressions);
                summaryDataVersionRule.setRuleExpressions(ruleExpressions);
            }
            default -> throw new IllegalArgumentException(String.format("Unknown type %s", ruleRequest.getType()));
        }
    }

    private void deleteAllRuleExpressionsByRuleIdAndExpressionIdNotIn(Long sDVRuleId, List<QualityRuleField> qualityRuleFields) {
        List<Long> ids = qualityRuleFields.stream().map(QualityRuleField::getId).toList();
        ruleExpressionRepository.deleteAllBySummaryDataVersionRuleIdAndIdNotIn(sDVRuleId, ids);
    }

    private RuleExpression getDefault(SummaryDataVersion summaryDataVersion) {
        int defaultNumber = 999;

        RuleExpression ruleExpression = new RuleExpression();
        ruleExpression.setType(ExpressionType.NO_EXPRESSION);
        ruleExpression.setSummaryDataVersionRule(summaryDataVersion);

        QualityOperation qualityOperation = new QualityOperation();
        qualityOperation.setRuleExpression(ruleExpression);
        qualityOperation.setNumber(defaultNumber);
        qualityOperation.setTextValue(Fields.UNDEFINED);
//        qualityOperation.setToIncidents(true);

        ruleExpression.setQualityOperation(qualityOperation);

        return ruleExpression;
    }

    private RuleExpression getRuleExpression(String condition, ExpressionType type, SummaryDataVersion summaryDataVersion, Long id) {
        RuleExpression ruleExpression;

        if (Objects.nonNull(id) && id != -1) {
            ruleExpression = ruleExpressionRepository.findByIdAndSummaryDataVersionRuleId(id, summaryDataVersion.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Rule expression '%s' not found", id)));
        } else {
            ruleExpression = new RuleExpression();
            ruleExpression.setType(type);
            ruleExpression.setSummaryDataVersionRule(summaryDataVersion);
        }
        ruleExpression.setExpression(condition);

        return ruleExpression;
    }

    private void saveToCacheRule(ModelRule modelRule, ComponentModel componentModel) {
        ModelRuleCacheDTO modelRuleCacheDTO = modelRuleCacheMapper.apply(modelRule, componentModel);
        String sDVRuleId = modelRule.getSummaryDataVersion().getId().toString();

        try {
            redisUtil.putValue(modelRuleRedisPrefix, sDVRuleId, modelRuleCacheDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Long createModelStruct(ModelStructRequestDTO modelStructRequestDTO) {
        Map<String, Object> map = ObjectMapperUtils.fromObjectToMap(modelStructRequestDTO);
        SummaryDataVersion summaryDataVersion = createSummaryDataVersion(map, SummaryDataType.MODEL);

        ModelStruct modelStruct;
        boolean isCreate = Boolean.TRUE.equals(map.get(Fields.IS_CREATE_FIELD));
        if (isCreate) {
            modelStruct = new ModelStruct();
        } else {
            modelStruct = modelStructRepository.findBySummaryDataVersionId(summaryDataVersion.getId())
                    .orElseThrow(() -> new SummaryDataVersionNotFoundException(summaryDataVersion.getId()));
            modelStructComponentsRepository.deleteAllById(modelStruct.getModelStructComponents()
                    .stream().map(ModelStructComponents::getId).toList());
            modelStruct.setModelStructComponents(Collections.emptyList());
        }

        modelStruct.setSummaryDataVersion(summaryDataVersion);
        modelStructRepository.save(modelStruct);
        modelStruct.setModelStructComponents(createComponent(modelStructRequestDTO, modelStruct));
//        checkModelStruct(modelStruct);

        return summaryDataVersion.getId();
    }

    private List<ModelStructComponents> createComponent(ModelStructRequestDTO modelStructRequestDTO, ModelStruct modelStruct) {
        List<ModelStructComponents> modelStructComponentsList = new ArrayList<>();

        for (SummaryDataType type : SummaryDataType.values()) {
            getModelComponentsList(modelStructComponentsList, modelStruct,
                    type.getComponentListFromDTO(modelStructRequestDTO), type);
        }

        List<ModelRuleRequestDTO> rules = modelStructRequestDTO.getRules();
        if (!rules.isEmpty()) {
            checkForRulesQueue(rules);
        }

        return modelStructComponentsList;
    }

    private void checkForRulesQueue(List<ModelRuleRequestDTO> ruleList) {
        int maxQueue = ruleList.stream()
                .mapToInt(ModelRuleRequestDTO::getQueueNumber)
                .max()
                .orElse(1);
        List<? extends ModelStructComponentsRequestDTO> maxQueueRules = ruleList.stream()
                .filter(r -> r.getQueueNumber().equals(maxQueue))
                .toList();
        boolean haveQuality = false;

        for (ModelStructComponentsRequestDTO r : maxQueueRules) {
            ModelRule modelRule = modelRuleRepository.findBySummaryDataVersionId(r.getComponentId())
                    .orElseThrow(() -> new SummaryDataVersionNotFoundException(r.getComponentId()));

            if (modelRule.getSummarySubType().equals(SummarySubType.QUALITY)) {
                haveQuality = true;
            }
        }

        if (!haveQuality) {
            throw new ApplicationException("The last rule in the queue should be the quality type rule", HttpStatus.BAD_REQUEST);
        }
    }

    private void getModelComponentsList(List<ModelStructComponents> components,
                                        ModelStruct modelStruct,
                                        List<? extends ModelStructComponentsRequestDTO> componentList,
                                        SummaryDataType type) {

        componentList.forEach(modelStructComponentsRequestDTO -> {
            Long versionId = modelStructComponentsRequestDTO.getComponentId();
            ModelStructComponents modelStructComponents = new ModelStructComponents();
            Object entityObject = connectorUtilService.findBySummaryDataVersionIdWithReflection(type, versionId);
            SummaryDataVersion summaryDataVersion = connectorUtilService.getSummaryDataVersionFromReflection(entityObject);

            switch (type) {
                case CONNECTOR_OUTPUT -> {
                    ModelConnectorOutputRequestDTO modelConnectorOutputRequestDTO = (ModelConnectorOutputRequestDTO) modelStructComponentsRequestDTO;
                    if (Objects.nonNull(modelConnectorOutputRequestDTO.getTest())) {
                        addComponentElement(modelStructComponents, ModelComponentEnumField.TEST, modelConnectorOutputRequestDTO.getTest());
                    }
                }
                case CONNECTOR_INPUT -> {
                    ModelConnectorInputRequestDTO modelConnectorInputRequestDTO = (ModelConnectorInputRequestDTO) modelStructComponentsRequestDTO;

                    String launchSecondStage = modelConnectorInputRequestDTO.getLaunchSecondStage() != null
                            ? modelConnectorInputRequestDTO.getLaunchSecondStage().toString()
                            : String.valueOf(Boolean.FALSE);
                    addComponentElement(modelStructComponents, ModelComponentEnumField.LAUNCH_SECOND_STAGE, launchSecondStage);
                }
                case RULE -> {
                    ModelRuleRequestDTO modelRuleRequestDTO = (ModelRuleRequestDTO) modelStructComponentsRequestDTO;
                    ModelRule modelRule = (ModelRule) entityObject;

                    addRuleComponentElements(modelRuleRequestDTO, modelStructComponents, modelRule);
                }
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            modelStructComponents.setModelStruct(modelStruct);
            modelStructComponents.setSummaryDataVersion(summaryDataVersion);
            modelStructComponents.setDaysRemaining(Objects.nonNull(modelStructComponentsRequestDTO.getDaysRemaining()) ? modelStructComponentsRequestDTO.getDaysRemaining() : 0);
            modelStructComponents.setResultIncremental(modelStructComponentsRequestDTO.getResultIncremental());

            modelStructComponentsRepository.save(modelStructComponents);
            components.add(modelStructComponents);
        });
    }

    private void addRuleComponentElements(ModelRuleRequestDTO modelRuleRequestDTO,
                                          ModelStructComponents modelStructComponents,
                                          ModelRule rule) {
        if (rule.getSummarySubType().equals(SummarySubType.QUALITY)) {
            if (Objects.nonNull(modelRuleRequestDTO.getConnectorOutputSDVId())) {
                Long connectorOutputSDVId = modelRuleRequestDTO.getConnectorOutputSDVId();
                modelConnectorOutputRepository.findBySummaryDataVersionId(connectorOutputSDVId)
                        .orElseThrow(() -> new SummaryDataVersionNotFoundException(connectorOutputSDVId));
                addComponentElement(modelStructComponents, ModelComponentEnumField.CONNECTOR_OUTPUT_SDV_ID, connectorOutputSDVId);
            }

            if (Objects.nonNull(modelRuleRequestDTO.getToIncidents())) {
                addComponentElement(modelStructComponents, ModelComponentEnumField.TO_INCIDENTS, modelRuleRequestDTO.getToIncidents());
        }}
        addComponentElement(modelStructComponents, ModelComponentEnumField.QUEUE_NUMBER, modelRuleRequestDTO.getQueueNumber());
    }

    private void addComponentElement(ModelStructComponents modelStructComponents, ModelComponentEnumField enumField, Object value) {
        ModelComponentElements modelComponentElement = new ModelComponentElements(
                modelStructComponents,
                enumField,
                enumField.getType().getSimpleName(),
                value.toString()
        );
        modelComponentElementsRepository.save(modelComponentElement);
        modelStructComponents.getModelComponentElements().add(modelComponentElement);
    }

//    private void checkModelStruct(ModelStruct modelStruct) {
//        for (SummaryDataType type : SummaryDataType.values()) {
//            List<ModelStructComponents> components = modelStruct.getModelStructComponents().stream().filter(m -> m.getSummaryDataVersion().getSummaryData().getType().equals(type)).toList();
//            if (!type.equals(SummaryDataType.MODEL) && components.isEmpty()) {
//                throw new ApplicationException("Model should have at least one: " + type.getError(), HttpStatus.BAD_REQUEST);
//            } else if (type.equals(SummaryDataType.RULE)) {
//                boolean validRules = components.stream()
//                        .map(r -> modelRuleRepository.findBySummaryDataVersionId(r.getSummaryDataVersion().getId()))
//                        .filter(Optional::isPresent)
//                        .map(Optional::get)
//                        .anyMatch(modelRule -> modelRule.getRuleType().equals(RuleType.QUALITY));
//                if (!validRules) {
//                    throw new ApplicationException("Model should have at least one rule QUALITY type", HttpStatus.BAD_REQUEST);
//                }
//            }
//        }
//    }

    @Override
    @Transactional
    public SummaryVersionState publishOrCancel(Long id, SummaryVersionState requestedState) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));
        List<SummaryDataVersion> publishedList = summaryDataVersionRepository.findAllBySummaryDataIdAndState(summaryDataVersion.getSummaryData().getId(), SummaryVersionState.PUBLISHED);

        SummaryVersionState responseState = summaryDataVersion.getState();

        if (requestedState.equals(SummaryVersionState.PUBLISHED) && responseState.equals(SummaryVersionState.DRAFT)) {
            summaryDataVersion.setState(SummaryVersionState.PUBLISHED);
            summaryDataVersion.setVersion((short) (findMaxVersion(summaryDataVersion.getSummaryData().getId()) + 1));

            if (publishedList.isEmpty()) {
                deactivateVersion(summaryDataVersion.getSummaryData().getId());
                summaryDataVersion.setIsActive(true);
                summaryDataVersion.setVersion((short) 1);
            }

            Optional<SummaryData> summaryDataOptional = summaryDataRepository.findById(summaryDataVersion.getSummaryData().getId());
            summaryDataOptional.ifPresent(connector -> connector.setVersion(summaryDataVersion.getVersion()));

        } else if (requestedState.equals(SummaryVersionState.CANCELED) && responseState.equals(SummaryVersionState.PUBLISHED)) {
            if (summaryDataVersion.getIsActive()) {
                throw new ApplicationException("Activated version can not be canceled", HttpStatus.BAD_REQUEST);
            } else {
                summaryDataVersion.setState(SummaryVersionState.CANCELED);
            }
        } else log.info("Canceled connector or already published!");

        summaryDataVersionRepository.save(summaryDataVersion);
        return summaryDataVersion.getState();
    }

    private Short findMaxVersion(UUID uuid) {
        Optional<SummaryDataVersion> summaryDataVersion = summaryDataVersionRepository.findFirstBySummaryDataIdOrderByVersionDesc(uuid);
        return summaryDataVersion
                .map(SummaryDataVersion::getVersion)
                .orElse((short) 1);
    }

    @Override
    public void deleteDraftOrCanceled(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));
        SummaryVersionState state = summaryDataVersion.getState();

        if ((state.equals(SummaryVersionState.DRAFT)
                || state.equals(SummaryVersionState.CANCELED))) {

            if (!summaryDataVersion.getSummaryData().getType().equals(SummaryDataType.MODEL)) {
                summaryFieldService.checkForFieldRelation(id, summaryDataVersion.getSummaryFields());
            }

            summaryDataVersionRepository.deleteById(id);

            if (summaryDataVersion.getSummaryData().getSummaryDataVersions().isEmpty()) {
                summaryDataRepository.deleteById(summaryDataVersion.getSummaryData().getId());
            }
        } else {
            throw new ApplicationException("Summary data version published and cannot be deleted", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Boolean changeActiveVersion(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));

        if (summaryDataVersion.getIsActive().equals(false)
                && !summaryDataVersion.getState().equals(SummaryVersionState.CANCELED)) {
            deactivateVersion(summaryDataVersion.getSummaryData().getId());
            summaryDataVersion.setIsActive(true);
            summaryDataVersionRepository.save(summaryDataVersion);
        } else log.info("Connector version is already active or its canceled");

        return summaryDataVersion.getIsActive();
    }

    private void deactivateVersion(UUID connectorId) {
        Optional<SummaryDataVersion> summaryDataVersionOptional = summaryDataVersionRepository.findBySummaryDataIdAndIsActive(connectorId, true);
        if (summaryDataVersionOptional.isPresent()) {
            summaryDataVersionOptional.get().setIsActive(false);
            summaryDataVersionRepository.save(summaryDataVersionOptional.get());
        } else log.error("Active connector version not exist");
    }

    @Override
    public SummaryDataListResponseDTO filter(SummaryDataFilterRequestDTO summaryDataFilterRequestDTO,
                                             Long page, Long size, Long userId,
                                             String orderBy, List<SummaryDataType> types) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SummaryDataVersion> criteriaQuery = cb.createQuery(SummaryDataVersion.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<SummaryDataVersion> root = criteriaQuery.from(SummaryDataVersion.class);
        Root<SummaryDataVersion> countRoot = countQuery.from(SummaryDataVersion.class);

        Predicate predicate = buildPredicate(summaryDataFilterRequestDTO, userId, types, cb, root, criteriaQuery);
        Predicate countPredicate = buildPredicate(summaryDataFilterRequestDTO, userId, types, cb, countRoot, criteriaQuery);

        criteriaQuery.where(predicate);
        if (Objects.isNull(orderBy) || orderBy.equals("desc")) {
            criteriaQuery.orderBy(cb.desc(root.get(SummaryDataVersion_.CREATE_DATE)));
        } else if (orderBy.equals("asc")) {
            criteriaQuery.orderBy(cb.asc(root.get(SummaryDataVersion_.CREATE_DATE)));
        }

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicate);

        TypedQuery<SummaryDataVersion> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) (page * size));
        typedQuery.setMaxResults(Math.toIntExact(size));

        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
        List<SummaryDataResponseDTO> summaryDataResponseDTOS = getSummaryDataResponseDTOS(summaryDataFilterRequestDTO, typedQuery);

        return new SummaryDataListResponseDTO(totalCount, summaryDataResponseDTOS);
    }

    private Predicate buildPredicate(SummaryDataFilterRequestDTO summaryDataFilterRequestDTO,
                                     Long userId, List<SummaryDataType> types,
                                     CriteriaBuilder cb, Root<SummaryDataVersion> root,
                                     CriteriaQuery<SummaryDataVersion> criteriaQuery) {
        Predicate predicate = cb.conjunction();
        Join<SummaryDataVersion, SummaryData> summaryDataJoin = root.join(SummaryDataVersion_.SUMMARY_DATA);

        if (Objects.nonNull(summaryDataFilterRequestDTO.getStartCreateDate())) {// date format 2022-10-22
            predicate = cb.and(predicate, filterConnectorsByDate(summaryDataFilterRequestDTO.getStartCreateDate(),
                    summaryDataFilterRequestDTO.getEndCreateDate(), SummaryDataVersion_.CREATE_DATE, cb, root));
        }

        if (Objects.nonNull(summaryDataFilterRequestDTO.getStartUpdateDate())) {// date format 2022-10-22
            predicate = cb.and(predicate, filterConnectorsByDate(summaryDataFilterRequestDTO.getStartUpdateDate(),
                    summaryDataFilterRequestDTO.getEndUpdateDate(), SummaryDataVersion_.UPDATE_DATE, cb, root));
        }

        if (Objects.nonNull(summaryDataFilterRequestDTO.getName())) {
            predicate = cb.and(predicate, filterConnectorsByNameOrShortDescription(summaryDataFilterRequestDTO.getName(),
                    SummaryDataVersion_.NAME, cb, root));
        }

        if (Objects.nonNull(summaryDataFilterRequestDTO.getShortDescription()) && !summaryDataFilterRequestDTO.getShortDescription().isEmpty()) {
            predicate = cb.and(predicate, filterConnectorsByNameOrShortDescription(summaryDataFilterRequestDTO.getShortDescription(),
                    SummaryDataVersion_.DESCRIPTION, cb, root));
        }

        if (Objects.nonNull(types) && !types.isEmpty()) {
            List<Predicate> typePredicates = types.stream()
                    .map(type -> cb.equal(summaryDataJoin.get(SummaryData_.TYPE), type))
                    .toList();
            Predicate predicate1 = typePredicates.stream()
                    .reduce(cb::or)
                    .orElse(cb.conjunction());

            predicate = cb.and(predicate, predicate1);
        } else {
            predicate = cb.and(predicate, cb.notEqual(summaryDataJoin.get(SummaryData_.TYPE), SummaryDataType.MODEL));
        }

        cb.and(predicate, cb.equal(summaryDataJoin.get(SummaryData_.USER_CREATE_ID), userId));
        cb.and(predicate, getStatePredicate(cb, root));

        if (Objects.nonNull(summaryDataFilterRequestDTO.getModelId())) {
            predicate = cb.and(predicate, modelIdPredicate(summaryDataFilterRequestDTO, criteriaQuery, cb, root));
        }

        return predicate;
    }

    private static Predicate getStatePredicate(CriteriaBuilder cb, Root<SummaryDataVersion> root) {
        Predicate isActivePredicate = cb.equal(root.get(SummaryDataVersion_.IS_ACTIVE), true);
        return cb.or(isActivePredicate, cb.equal(root.get(SummaryDataVersion_.STATE), SummaryVersionState.DRAFT));
    }

    private static Predicate modelIdPredicate(SummaryDataFilterRequestDTO summaryDataFilterRequestDTO,
                                         CriteriaQuery<SummaryDataVersion> criteriaQuery,
                                         CriteriaBuilder cb, Root<SummaryDataVersion> root) {
        Subquery<ModelStructComponents> sq = criteriaQuery.subquery(ModelStructComponents.class);
        Root<ModelStructComponents> mscRoot = sq.from(ModelStructComponents.class);

        sq.select(mscRoot.get(ModelStructComponents_.SUMMARY_DATA_VERSION))
                .where(cb.equal(mscRoot.get(ModelStructComponents_.MODEL_STRUCT).get(ModelStructComponents_.SUMMARY_DATA_VERSION), summaryDataFilterRequestDTO.getModelId()));
        return cb.in(root.get(SummaryDataVersion_.ID)).value(sq);
    }

    private List<SummaryDataResponseDTO> getSummaryDataResponseDTOS(SummaryDataFilterRequestDTO summaryDataFilterRequestDTO,
                                                                    TypedQuery<SummaryDataVersion> typedQuery) {
        List<SummaryDataVersion> resultList = typedQuery.getResultList();

        if (resultList.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Long> resultListIds = resultList.stream()
                    .map(SummaryDataVersion::getUserUpdateId)
                    .distinct()
                    .toList();

            Map<Long, String> userNames = getNames(resultListIds);

            return resultList.stream()
                    .map(r -> {
                        SummaryDataResponseDTO summaryDataResponseDTO = summaryDataDTOMapper.buildVersion(r, summaryDataFilterRequestDTO.getShowFields(), r.getSummaryData().getType());
                        summaryDataResponseDTO.setUserName(userNames.get(r.getUserUpdateId()));
                        return summaryDataResponseDTO;
                    })
                    .toList();
        }
    }

    private Map<Long, String> getNames(List<Long> resultList) {
        UserListDTO userListDTO = adminServiceClient.getUsers(resultList).getBody();

        if (Objects.isNull(userListDTO) || Objects.isNull(userListDTO.getUserResponseDTOS())) {
            throw new ApplicationException("Users not found", HttpStatus.NOT_FOUND);
        }

        return userListDTO.getUserResponseDTOS().stream()
                .collect(Collectors.toMap(UserResponseDTO::getId,
                        UserResponseDTO::getCompany, (k, v) -> k, HashMap::new));
    }

    private Predicate filterConnectorsByNameOrShortDescription(String name, String column,
                                                               CriteriaBuilder criteriaBuilder,
                                                               Root<SummaryDataVersion> root) {
        return criteriaBuilder.like(criteriaBuilder.lower(root.get(column)), "%" + name.toLowerCase() + "%");
    }

    private Predicate filterConnectorsByDate(Date startDate, Date endDate,
                                             String column,
                                             CriteriaBuilder criteriaBuilder,
                                             Root<SummaryDataVersion> root) {
        Predicate datePredicate = null;
        if (Objects.nonNull(startDate)) {
            datePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(column), startDate);
        }
        if (Objects.nonNull(endDate)) {
            if (datePredicate == null) {
                datePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(column),
                        endDate.toInstant().plus(Duration.ofDays(1)));

            } else {
                datePredicate = criteriaBuilder.and(datePredicate, criteriaBuilder.lessThanOrEqualTo(root.get(column),
                        endDate.toInstant().plus(Duration.ofDays(1))));
            }
        }
        if (datePredicate == null) {
            datePredicate = criteriaBuilder.lessThanOrEqualTo(root.get(column),
                    new Date().toInstant().plus(Duration.ofDays(1)));
        }
        return datePredicate;
    }

//    private List<ModelConnectorInputDTO> getModelConnectorInputs() {
//        return modelConnectorInputRepository.findAll().stream()
//                .map(modelConnectorInputDTOMapper::buildModelConnectorInputDTO)
//                .toList();
//    }
//
//    private List<ModelConnectorOutputDTO> getModelConnectorOutputs() {
//        return modelConnectorOutputRepository.findAll().stream()
//                .map(modelConnectorOutputDTOMapper::buildModelConnectorOutputDTO)
//                .toList();
//    }

    @Override
    public List<ModelConnectorDTO> getAllConnectors() {
        List<ModelConnectorOutput> inputs = modelConnectorOutputRepository.findAll();
        List<ModelConnectorInput> outputs = modelConnectorInputRepository.findAll();
        List<ModelConnectorDTO> combinedDTOs = new ArrayList<>();

        inputs.forEach(input -> combinedDTOs.add(new ModelConnectorDTO(
                input.getSummaryDataVersion().getId(),
                input.getSummaryDataVersion().getUserUpdateId(),
                input.getSummaryDataVersion().getName(),
                input.getSummaryDataVersion().getSummaryData().getType(),
                input.getSummarySubType(),
                input.getDataFormat(),
                input.getSummaryDataVersion().getCommon()
        )));

        outputs.forEach(output -> combinedDTOs.add(new ModelConnectorDTO(
                output.getSummaryDataVersion().getId(),
                output.getSummaryDataVersion().getUserUpdateId(),
                output.getSummaryDataVersion().getName(),
                output.getSummaryDataVersion().getSummaryData().getType(),
                output.getSummarySubType(),
                output.getDataFormat(),
                output.getSummaryDataVersion().getCommon()
        )));

        return combinedDTOs.stream()
                .sorted(Comparator.comparingLong(ModelConnectorDTO::getId).reversed())
                .toList();
    }

    @Override
    public List<ModelRuleDTO> getModelRules() {
        return modelRuleRepository.findAll().stream()
                .map(modelRuleDTOMapper::buildModelRuleDTO)
                .toList();
    }

    @Override
    public List<ModelStructGetResponseDTO> getModels() {
        return modelStructRepository.findAll().stream()
                .map(getResponseDTOMapper::buildGetModel)
                .toList();
    }

    private ModelConnectorOutputGetResponseDTO getModelConnectorOutput(Long id) {
        return getResponseDTOMapper.buildModelConnectorOutputGetResponseDTO(modelConnectorOutputRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model Connector Output not found: %d", id), HttpStatus.NOT_FOUND)));
    }

    private ModelConnectorInputGetResponseDTO getModelConnectorInput(Long id) {
        return getResponseDTOMapper.buildModelConnectorInputGetResponseDTO(modelConnectorInputRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model Connector Input not found: %d", id), HttpStatus.NOT_FOUND)));
    }

    @Override
    public Object getConnector(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));

        if (summaryDataVersion.getSummaryData().getType().equals(SummaryDataType.CONNECTOR_INPUT)) {
            return getModelConnectorInput(id);
        }
        return getModelConnectorOutput(id);
    }

    @Override
    public ModelRuleGetResponseDTO getModelRule(Long id, Long modelId) {
        return getResponseDTOMapper.buildModelRuleGetResponseDTO(modelRuleRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model Rule not found: %d", id), HttpStatus.NOT_FOUND)), modelId);
    }

    @Override
    public SummaryDataResponseDTO getModel(Long id) {
        SummaryDataVersion summaryDataVersion = modelStructRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id))
                .getSummaryDataVersion();

        SummaryDataResponseDTO summaryDataResponseDTO = summaryDataDTOMapper.buildVersion(summaryDataVersion, false);
        summaryDataResponseDTO.setUserName(getResponseDTOMapper.getName(summaryDataVersion));

        return summaryDataResponseDTO;
    }

    @Override
    public ModelConnectorOutputDTO getModelConnectorOutputByVersionId(Long sDVId) {
        return modelConnectorOutputRepository.findBySummaryDataVersionId(sDVId).map(modelConnectorOutputDTOMapper::buildModelConnectorOutputDTO)
                .orElseThrow(() -> new NotFoundException(String.format("Model Connector Output not found with id %s", sDVId)));
    }

    private SummaryDataVersion duplicateSummaryData(Object object, SummaryDataVersion s) {
        Map<String, Object> map = ObjectMapperUtils.fromObjectToMap(summaryDataDTOMapper.buildVersion(s, false));

        map.put("state", null);
        map.put(Fields.IS_CREATE_FIELD, true);
        map.put(Fields.TYPE_FIELD, s.getSummaryData().getType());

        if (s.getName().contains("copy")) {
            Pattern pattern = Pattern.compile("\\b(copy)(\\d+)\\b");
            Matcher matcher = pattern.matcher(s.getName());
            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                int number = Integer.parseInt(matcher.group(2)) + 1;
                matcher.appendReplacement(result, matcher.group(1) + number);
            }
            matcher.appendTail(result);
            map.put("name", result.toString());
        } else {
            map.put("name", s.getName() + " copy1");
        }
        map.put("description", s.getDescription());

        SummaryDataVersion summaryDataVersion = createSummaryDataVersion(map, s.getSummaryData().getType());
        if (Objects.nonNull(object)) {
            connectorUtilService.getDeclaredFields(object, map);
        }

        return summaryDataVersion;
    }

    @Transactional
    public Long duplicateConnector(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));

        if (summaryDataVersion.getSummaryData().getType().equals(SummaryDataType.CONNECTOR_INPUT)) {
            return duplicateInputConnector(id);
        }
        return duplicateOutputConnector(id);
    }

    private Long duplicateInputConnector(Long id) {
        ModelConnectorInput modelConnectorInput = modelConnectorInputRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model Connector Input not found: %d", id), HttpStatus.NOT_FOUND));
        ModelConnectorInput duplicateModelConnectorInput = new ModelConnectorInput();
        SummaryDataVersion duplicatedSummaryDataVersion = duplicateSummaryData(duplicateModelConnectorInput, modelConnectorInput.getSummaryDataVersion());

        duplicateModelConnectorInput.setSummaryDataVersion(duplicatedSummaryDataVersion);
        duplicateModelConnectorInput.setSummarySubType(modelConnectorInput.getSummarySubType());
        List<SummaryField> duplicatedFields = summaryFieldService.duplicateFields(modelConnectorInput.getSummaryDataVersion(), duplicateModelConnectorInput.getSummaryDataVersion());
        duplicateModelConnectorInput.getSummaryDataVersion().setSummaryFields(duplicatedFields);
        modelConnectorInputRepository.save(duplicateModelConnectorInput);

        return duplicateModelConnectorInput.getSummaryDataVersion().getId();
    }

    private Long duplicateOutputConnector(Long id) {
        ModelConnectorOutput modelConnectorOutput = modelConnectorOutputRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model Connector Output not found: %d", id), HttpStatus.NOT_FOUND));
        ModelConnectorOutput duplicateModelConnectorOutput = new ModelConnectorOutput();
        SummaryDataVersion duplicatedSummaryDataVersion = duplicateSummaryData(duplicateModelConnectorOutput, modelConnectorOutput.getSummaryDataVersion());

        duplicateModelConnectorOutput.setSummaryDataVersion(duplicatedSummaryDataVersion);
        duplicateModelConnectorOutput.setSummarySubType(modelConnectorOutput.getSummarySubType());
        List<SummaryField> duplicatedFields = summaryFieldService.duplicateFields(modelConnectorOutput.getSummaryDataVersion(), duplicateModelConnectorOutput.getSummaryDataVersion());
        duplicateModelConnectorOutput.getSummaryDataVersion().setSummaryFields(duplicatedFields);
        modelConnectorOutputRepository.save(duplicateModelConnectorOutput);

        return duplicateModelConnectorOutput.getSummaryDataVersion().getId();
    }

    @Override
    public Long duplicateRule(Long id) {
        ModelRule modelRule = modelRuleRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model Rule not found: %d", id), HttpStatus.NOT_FOUND));
        ModelRule duplicateModelRule = new ModelRule();

        duplicateModelRule.setSummaryDataVersion(duplicateSummaryData(duplicateModelRule, modelRule.getSummaryDataVersion()));
        List<SummaryField> duplicatedFields = summaryFieldService.duplicateFields(modelRule.getSummaryDataVersion(), duplicateModelRule.getSummaryDataVersion());
        duplicateModelRule.getSummaryDataVersion().setSummaryFields(duplicatedFields);
        modelRuleRepository.save(duplicateModelRule);

        return duplicateModelRule.getSummaryDataVersion().getId();
    }

    @Override
    public Long duplicateModel(Long id) {
        ModelStruct modelStruct = modelStructRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new ApplicationException(String.format("Model struct not found: %d", id), HttpStatus.NOT_FOUND));
        ModelStruct duplicateModelStruct = new ModelStruct();
        List<ModelStructComponents> modelStructComponents = modelStructComponentsRepository.findAllByModelStructId(modelStruct.getId());
        List<ModelStructComponents> duplicatedModelStructComponents = new ArrayList<>();

        duplicateModelStruct.setSummaryDataVersion(duplicateSummaryData(duplicateModelStruct, modelStruct.getSummaryDataVersion()));
        duplicateComponents(modelStructComponents, duplicateModelStruct, duplicatedModelStructComponents);
        duplicateModelStruct.setModelStructComponents(duplicatedModelStructComponents);
        modelStructRepository.save(duplicateModelStruct);

        return duplicateModelStruct.getSummaryDataVersion().getId();
    }

    private void duplicateComponents(List<ModelStructComponents> modelStructComponents, ModelStruct duplicateModelStruct, List<ModelStructComponents> duplicatedModelStructComponents) {
        for (ModelStructComponents m : modelStructComponents) {
            ModelStructComponents modelStructComponent = new ModelStructComponents();
            modelStructComponent.setSummaryDataVersion(m.getSummaryDataVersion());
            modelStructComponent.setModelStruct(duplicateModelStruct);
//            modelStructComponent.setLaunchSecondStage(m.getLaunchSecondStage());
//            modelStructComponent.setQueueNumber(m.getQueueNumber());
            modelStructComponent.setDaysRemaining(m.getDaysRemaining());
            modelStructComponent.setResultIncremental(m.getResultIncremental());
            duplicatedModelStructComponents.add(modelStructComponent);
        }
    }

    @Override
    public List<SummaryDataResponseDTO> historyOfChanges(Long id) {
        SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(id)
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(id));
        return summaryDataVersionRepository.findAllBySummaryDataId(summaryDataVersion.getSummaryData().getId())
                .stream()
                .map(s -> summaryDataDTOMapper.buildVersion(s, false))
                .toList();
    }

    @Override
    public ModelConnectorInput getModelConnectorInputEntity(Long summaryDataVersionId) {
        return modelConnectorInputRepository.findBySummaryDataVersionId(summaryDataVersionId)
                .orElseThrow(() -> new NotFoundException("Model Connector Input not found"));
    }

    @Override
    public Map<Long, ModelRuleCacheDTO> getModelRulesCacheDTOByIds(Map<Long, ComponentModel> modelRuleComponents) {
        List<String> keys = modelRuleComponents.keySet().stream().map(id -> toString()).toList();
        List<Object> objects = redisUtil.getAllValueByHashKeyAndKeys(modelRuleRedisPrefix, keys);

        List<ModelRuleCacheDTO> modelRuleCacheDTOList;
        if (Objects.isNull(objects) || objects.size() != modelRuleComponents.size() || objects.contains(null)) {

            List<ModelRule> modelRules = modelRuleRepository
                    .findBySummaryDataVersionIdIn(modelRuleComponents.keySet().stream().toList());
            modelRuleCacheDTOList = modelRules
                    .stream()
                    .map(rule -> modelRuleCacheMapper.apply(rule, modelRuleComponents.get(rule.getSummaryDataVersion().getId())))
                    .toList();
            log.info("ModelRuleCacheDTO list from db: " + modelRuleCacheDTOList);

            log.info("Saving ModelRuleCacheDTO list to cache...");
            modelRules.forEach(rule -> saveToCacheRule(rule, modelRuleComponents.get(rule.getSummaryDataVersion().getId())));
        } else {
            modelRuleCacheDTOList = objects.stream()
                    .map(o -> (ModelRuleCacheDTO) o)
                    .toList();
            log.info("ModelRuleCacheDTO list from cache: " + modelRuleCacheDTOList);
        }

        return getMapFromListModelRuleCache(modelRuleCacheDTOList);
    }

    @Override
    public Object getAccessFields(Long sDVModelId, Long sdvId) {
        List<ModelComponentElements> modelComponentElements = modelComponentElementsRepository.findAllByModelStructComponent_ModelStructId_SummaryDataVersionId(sDVModelId);
        if (modelComponentElements.isEmpty()) {
            throw new ApplicationException(String.format("Model version not found %d", sDVModelId), HttpStatus.NOT_FOUND);
        }
        Long ruleSDVId = getSdvId(sdvId, modelComponentElements);

        Optional<Integer> currentQueueNumber = modelComponentElements.stream()
                .filter(v -> v.getModelStructComponent().getSummaryDataVersion().getId().equals(ruleSDVId))
                .map(f -> (Integer) connectorUtilService.getComponentElement(f.getModelStructComponent(), ModelComponentEnumField.QUEUE_NUMBER))
                .findAny();
        if (currentQueueNumber.isPresent()) {
            Set<ModelComponentElements> uniqueSet = new HashSet<>(modelComponentElements);
            return uniqueSet.stream()
                    .filter(v -> !v.getModelStructComponent().getSummaryDataVersion().getSummaryData().getType().equals(SummaryDataType.CONNECTOR_OUTPUT))
                    .filter(v -> !v.getModelStructComponent().getSummaryDataVersion().getSummaryData().getType().equals(SummaryDataType.RULE)
                            || (currentQueueNumber.get() > (Integer) connectorUtilService.getComponentElement(v.getModelStructComponent(), ModelComponentEnumField.QUEUE_NUMBER)))
                    .map(b -> summaryFieldDTOMapper.buildAccessFieldDTO(b.getModelStructComponent()))
                    .filter(v -> !v.getFields().isEmpty())
                    .toList();
        }
        throw new SummaryDataVersionNotFoundException(sdvId);
    }

    private Long getSdvId(Long sdvId, List<ModelComponentElements> modelComponentElements) {
        SummaryDataVersion sdv = modelComponentElements.stream()
                .map(v -> v.getModelStructComponent().getSummaryDataVersion())
                .filter(v -> v.getId().equals(sdvId))
                .findAny()
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(sdvId));

        if (sdv.getSummaryData().getType().equals(SummaryDataType.CONNECTOR_OUTPUT)) {
            int maxQueue = modelComponentElements.stream()
                    .mapToInt(v -> Objects.requireNonNullElse(connectorUtilService.getComponentElement(v.getModelStructComponent(), ModelComponentEnumField.QUEUE_NUMBER), 0))
                    .max()
                    .orElse(0);

            Optional<ModelStructComponents> lastRule = modelComponentElements.stream()
                    .filter(f -> Objects.nonNull(connectorUtilService.getComponentElement(f.getModelStructComponent(), ModelComponentEnumField.QUEUE_NUMBER)) && connectorUtilService.getComponentElement(f.getModelStructComponent(), ModelComponentEnumField.QUEUE_NUMBER).equals(maxQueue))
                    .map(ModelComponentElements::getModelStructComponent)
                    .findFirst();

            if (lastRule.isPresent()) {
                return lastRule.get().getSummaryDataVersion().getId();
            }
        } else if (sdv.getSummaryData().getType().equals(SummaryDataType.CONNECTOR_INPUT)) {
            throw new ApplicationException("It's CONNECTOR INPUT, but it should be CONNECTOR OUTPUT or RULE", HttpStatus.BAD_REQUEST);
        }
        return sdvId;
    }


    private Map<Long, ModelRuleCacheDTO> getMapFromListModelRuleCache(List<ModelRuleCacheDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(ModelRuleCacheDTO::getSummaryDataVersionId, Function.identity()));
    }

    @Override
    public List<ComponentModel> getModelComponents(Long summaryDataVersionId) {
        Optional<ModelStruct> modelStructOptional = modelStructRepository.findBySummaryDataVersionId(summaryDataVersionId);
        if (modelStructOptional.isPresent()) {
            ModelStruct modelStruct = modelStructOptional.get();
            return modelStruct.getModelStructComponents().stream().map(modelComponentCacheMapper).toList();
        }
        throw new NotFoundException("Model Struct not found");
    }

    @Override
    public String getUrlConnectorOutput(Long id) {
        ModelConnectorOutput modelConnectorOutput = modelConnectorOutputRepository.findBySummaryDataVersionId(id)
                .orElseThrow(() -> new NotFoundException(String.format("Connector Output not found with id '%s'", id)));

        return modelConnectorOutput.getUrl();
    }
}