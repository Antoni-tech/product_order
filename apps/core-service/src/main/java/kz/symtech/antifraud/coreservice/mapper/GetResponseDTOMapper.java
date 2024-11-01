package kz.symtech.antifraud.coreservice.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsFilterResponseDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryConnectorFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryRuleFieldDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.*;
import kz.symtech.antifraud.coreservice.entities.models.*;
import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import kz.symtech.antifraud.coreservice.entities.rules.QualityOperation;
import kz.symtech.antifraud.coreservice.entities.rules.QualityRuleField;
import kz.symtech.antifraud.coreservice.entities.rules.QuantityRule;
import kz.symtech.antifraud.coreservice.entities.rules.RuleExpression;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.exceptions.ApplicationException;
import kz.symtech.antifraud.coreservice.exceptions.SummaryDataVersionNotFoundException;
import kz.symtech.antifraud.coreservice.repository.*;
import kz.symtech.antifraud.coreservice.services.ConnectorUtilService;
import kz.symtech.antifraud.feignclients.clients.services.AdminServiceClient;
import kz.symtech.antifraud.models.dto.UserResponseDTO;
import kz.symtech.antifraud.models.enums.ExpressionType;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import kz.symtech.antifraud.models.enums.SummarySubType;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetResponseDTOMapper {
    private final ModelStructComponentsRepository modelStructComponentsRepository;
    private final SummaryFieldDTOMapper summaryFieldDTOMapper;
    private final AdminServiceClient adminServiceClient;
    private final FieldRelationRepository fieldRelationRepository;
    private final RuleExpressionRepository ruleExpressionRepository;
    private final QualityOperationRepository qualityOperationRepository;
    private final SummaryDataVersionRepository summaryDataVersionRepository;
    private final ModelStructComponentsFilterResponseDTOMapper modelStructComponentsFilterResponseDTOMapper;
    private final ConnectorUtilService connectorUtilService;

    public ModelConnectorInputGetResponseDTO buildModelConnectorInputGetResponseDTO(ModelConnectorInput modelConnectorInput) {
        SummaryDataVersion s = modelConnectorInput.getSummaryDataVersion();
        List<SummaryConnectorFieldDTO> fields = ObjectMapperUtils.convertValue(summaryFieldDTOMapper.buildSummaryFieldDTOWithHierarchy(s.getSummaryFields()), new TypeReference<>() {
        });

        return ModelConnectorInputGetResponseDTO.builder()
                .summaryDataId(s.getSummaryData().getId())
                .versionId(s.getId())
                .userId(s.getUserUpdateId())
                .state(s.getState())
                .version(s.getVersion())
                .isActive(s.getIsActive())
                .createDate(s.getCreateDate())
                .type(s.getSummaryData().getType())
                .name(s.getName())
                .description(s.getDescription())
                .userName(getName(s))
                .connectorPurpose(modelConnectorInput.getConnectorPurpose())
                .connectorSubspecies(modelConnectorInput.getConnectorSubspecies())
                .technology(modelConnectorInput.getTechnology())
                .dataFormat(modelConnectorInput.getDataFormat())
                .urlForInfo(modelConnectorInput.getUrlForInfo())
                .cron(modelConnectorInput.getCron())
                .inputDataType(modelConnectorInput.getInputDataType())
                .summarySubType(modelConnectorInput.getSummarySubType())
                .connectorProtocol(modelConnectorInput.getConnectorProtocol())
                .connectorSubType(modelConnectorInput.getConnectorSubType())
                .extended(getExtendDataStructure(s.getExtended()))
                .models(getModels(s))
                .fields(fields)
                .build();
    }

    private ExtendDataStructureGetResponseDTO getExtendDataStructure(Long extended) {
        if (Objects.nonNull(extended)) {
            SummaryDataVersion summaryDataVersion = summaryDataVersionRepository.findById(extended)
                    .orElseThrow(() -> new SummaryDataVersionNotFoundException(extended));

            return ExtendDataStructureGetResponseDTO.builder()
                    .extendedVersionId(summaryDataVersion.getId())
                    .isActive(summaryDataVersion.getIsActive())
                    .build();
        }
        return null;
    }

    private List<ModelStructComponentsFilterResponseDTO> getModels(SummaryDataVersion s) {
        return modelStructComponentsRepository.findAllBySummaryDataVersionId(s.getId())
                .stream()
                .map(modelStructComponentsFilterResponseDTOMapper)
                .toList();
    }

    public ModelConnectorOutputGetResponseDTO buildModelConnectorOutputGetResponseDTO(ModelConnectorOutput modelConnectorOutput) {
        SummaryDataVersion s = modelConnectorOutput.getSummaryDataVersion();
        List<SummaryConnectorFieldDTO> fields = ObjectMapperUtils.convertValue(summaryFieldDTOMapper.buildSummaryFieldDTOWithHierarchy(s.getSummaryFields()), new TypeReference<>() {
        });

        return ModelConnectorOutputGetResponseDTO.builder()
                .summaryDataId(s.getSummaryData().getId())
                .versionId(s.getId())
                .userId(s.getUserUpdateId())
                .state(s.getState())
                .version(s.getVersion())
                .isActive(s.getIsActive())
                .createDate(s.getCreateDate())
                .type(s.getSummaryData().getType())
                .name(s.getName())
                .description(s.getDescription())
                .userName(getName(s))
                .url(modelConnectorOutput.getUrl())
                .checkMainAttributes(modelConnectorOutput.getCheckMainAttributes())
                .dataFormat(modelConnectorOutput.getDataFormat())
                .urlForInfo(modelConnectorOutput.getUrlForInfo())
                .cron(modelConnectorOutput.getCron())
                .summarySubType(modelConnectorOutput.getSummarySubType())
                .connectorProtocol(modelConnectorOutput.getConnectorProtocol())
                .connectorSubType(modelConnectorOutput.getConnectorSubType())
                .extended(getExtendDataStructure(s.getExtended()))
                .models(getModels(s))
                .fields(fields)
                .build();
    }

    public ModelRuleGetResponseDTO buildModelRuleGetResponseDTO(ModelRule modelRule, Long modelId) {
        SummaryDataVersion s = modelRule.getSummaryDataVersion();
        List<SummaryRuleFieldDTO> fields = ObjectMapperUtils.convertValue(summaryFieldDTOMapper.buildSummaryFieldDTOWithHierarchy(s.getSummaryFields()), new TypeReference<>() {
        });
        Object ruleCondition = null;

        if (modelRule.getSummarySubType().equals(SummarySubType.QUANTITY)) {
            List<RuleExpression> ruleExpressions = ruleExpressionRepository.findAllBySummaryDataVersionRuleId(s.getId());
            if (!ruleExpressions.isEmpty()) {
                List<QuantityRule> quantityRules = new ArrayList<>();
                Optional<String> conditionExpression = ruleExpressions.stream()
                        .filter(v -> v.getType() == ExpressionType.CONDITION)
                        .map(RuleExpression::getExpression)
                        .findFirst();
                Optional<String> operationExpression = ruleExpressions.stream()
                        .filter(v -> v.getType() == ExpressionType.OPERATION)
                        .map(RuleExpression::getExpression)
                        .findFirst();
                QuantityRule quantityRule = new QuantityRule();
                conditionExpression.ifPresent(quantityRule::setCondition);
                operationExpression.ifPresent(quantityRule::setResultCondition);
                quantityRules.add(quantityRule);
                ruleCondition = quantityRules;
            }
        }

        if (modelRule.getSummarySubType().equals(SummarySubType.QUALITY)) {
            List<QualityOperation> qualityOperations = qualityOperationRepository.findAllByRuleExpressionSummaryDataVersionRuleId(s.getId());
            if (!qualityOperations.isEmpty()) {
                List<QualityRuleField> qualityRuleFields = new ArrayList<>();
                qualityOperations.forEach(qo -> {
                    QualityRuleField qualityRuleField = new QualityRuleField();
                    qualityRuleField.setId(qo.getRuleExpression().getId());
                    qualityRuleField.setCondition(qo.getRuleExpression().getExpression());
                    qualityRuleField.setNumber(qo.getNumber());
//                    qualityRuleField.setConnectorOutputVersionId(Objects.nonNull(qo.getSummaryDataVersionConnectorOutput()) ? qo.getSummaryDataVersionConnectorOutput().getId() : null);
                    qualityRuleField.setTextValue(qo.getTextValue());
//                    qualityRuleField.setToIncidents(qo.getToIncidents());
                    qualityRuleField.setType(qo.getRuleExpression().getType());
                    qualityRuleFields.add(qualityRuleField);
                });
                ruleCondition = qualityRuleFields;
            }
        }

        ModelStructComponents component = null;
        if (Objects.nonNull(modelId)) {
            component = modelStructComponentsRepository.findBySummaryDataVersionId(modelRule.getSummaryDataVersion().getId());
            List<FieldRelation> srcFields = fieldRelationRepository.findAllByModelStructId(modelId);
            fields.forEach(v -> srcFields.stream().filter(srcF -> v.getId().equals(srcF.getVarSummaryField().getId()))
                    .findFirst()
                    .ifPresent(fieldRelation -> v.setSrcRelationId(fieldRelation.getId())));
        }


        return ModelRuleGetResponseDTO.builder()
                .summaryDataId(s.getSummaryData().getId())
                .versionId(s.getId())
                .userId(s.getUserUpdateId())
                .state(s.getState())
                .version(s.getVersion())
                .isActive(s.getIsActive())
                .createDate(s.getCreateDate())
                .type(s.getSummaryData().getType())
                .name(s.getName())
                .description(s.getDescription())
                .userName(getName(s))
                .summarySubType(modelRule.getSummarySubType())
                .jsonData(ruleCondition)
                .fields(fields)
                .daysRemaining(Objects.nonNull(component) ? component.getDaysRemaining() : null)
                .resultIncremental(Objects.nonNull(component) ? component.getResultIncremental() : null)
                .extended(getExtendDataStructure(s.getExtended()))
                .models(getModels(s))
                .build();
    }

    public ModelStructGetResponseDTO buildGetModel(ModelStruct modelStruct) {
        return ModelStructGetResponseDTO.builder()
                .modelId(modelStruct.getId())
                .userId(modelStruct.getSummaryDataVersion().getUserUpdateId())
                .userName(getName(modelStruct.getSummaryDataVersion()))
                .modelName(modelStruct.getSummaryDataVersion().getName())
                .summaryDataVersionId(modelStruct.getSummaryDataVersion().getId())
                .components(modelStruct.getModelStructComponents().stream()
                        .map(m ->
                                ModelStructComponentsDTO.builder()
                                        .modelComponentId(m.getSummaryDataVersion().getId())
                                        .name(m.getSummaryDataVersion().getName())
                                        .summaryState(m.getSummaryDataVersion().getState())
                                        .type(m.getSummaryDataVersion().getSummaryData().getType())
                                        .launchSecondStage(connectorUtilService.getComponentElement(m, ModelComponentEnumField.LAUNCH_SECOND_STAGE))
                                        .queueNumber(connectorUtilService.getComponentElement(m, ModelComponentEnumField.QUEUE_NUMBER))
                                        .daysRemaining(m.getDaysRemaining())
                                        .resultIncremental(m.getResultIncremental())
                                        .build())
                        .toList())
                .build();
    }

    public String getName(SummaryDataVersion v) {
        UserResponseDTO user = adminServiceClient.getUser(String.valueOf(v.getUserUpdateId())).getBody();

        if (Objects.isNull(user)) {
            throw new ApplicationException("User with this id not found: " + v.getUserUpdateId(), HttpStatus.NOT_FOUND);
        }

        return user.getCompany();
    }
}
