package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.filter.response.*;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorInput;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorOutput;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryData;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.exceptions.ApplicationException;
import kz.symtech.antifraud.coreservice.exceptions.SummaryDataVersionNotFoundException;
import kz.symtech.antifraud.coreservice.repository.*;
import kz.symtech.antifraud.coreservice.services.ConnectorUtilService;
import kz.symtech.antifraud.coreservice.services.CountDocumentService;
import kz.symtech.antifraud.coreservice.services.TransactionCounterService;
import kz.symtech.antifraud.feignclients.clients.services.AdminServiceClient;
import kz.symtech.antifraud.models.dto.UserListDTO;
import kz.symtech.antifraud.models.dto.UserResponseDTO;
import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import kz.symtech.antifraud.models.enums.SummarySubType;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static kz.symtech.antifraud.models.utils.ObjectMapperUtils.objectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryDataDTOMapper {

    private final CountDocumentService countDocumentService;
    private final ModelStructComponentsRepository modelStructComponentsRepository;
    private final ModelConnectorInputRepository modelConnectorInputRepository;
    private final ModelConnectorOutputRepository modelConnectorOutputRepository;
    private final ModelStructRepository modelStructRepository;
    private final TransactionCounterService transactionCounterService;
    private final SummaryFieldDTOMapper summaryFieldDTOMapper;
    private final AdminServiceClient adminServiceClient;
    private final ConnectorUtilService connectorUtilService;
    private final ModelStructComponentsFilterResponseDTOMapper modelStructComponentsFilterResponseDTOMapper;

    private static final Long USERID = 1L;

    public SummaryDataResponseDTO buildVersion(SummaryDataVersion v, Boolean showFields) {
        SummaryDataType summaryDataType = v.getSummaryData().getType();

        return buildVersion(v, showFields, summaryDataType);
    }

    public SummaryDataResponseDTO buildVersion(SummaryDataVersion v, Boolean showFields, SummaryDataType summaryDataType) {
        SummaryDataResponseDTO summaryDataResponseDTO = new SummaryDataResponseDTO();

        if (Objects.nonNull(summaryDataType)) {//for data structure get all
            if (summaryDataType == SummaryDataType.MODEL) {
                summaryDataResponseDTO = modelMapping(v, showFields);
            } else if (summaryDataType == SummaryDataType.CONNECTOR_INPUT) {
                summaryDataResponseDTO = modelConnectorInputMapping(v);
            } else if (summaryDataType == SummaryDataType.CONNECTOR_OUTPUT) {
                summaryDataResponseDTO = modelConnectorOutputMapping(v);
            } else if (summaryDataType == SummaryDataType.RULE) {
                summaryDataResponseDTO = new ModelRuleFilterDTO();
            } else if (summaryDataType == SummaryDataType.DATA_STRUCTURE) {
                summaryDataResponseDTO = new DataStructureFilterDTO();
            }
        } else {
            summaryDataResponseDTO = new SummaryDataSubResponseDTO();
        }

        if (summaryDataType != SummaryDataType.MODEL) {
            SummaryDataSubResponseDTO summaryDataSubResponseDTO = (SummaryDataSubResponseDTO) summaryDataResponseDTO;
            summaryDataSubResponseDTO.setSummarySubType(getSubtype(v));
            summaryDataSubResponseDTO.setModels(getModels(v));
            summaryDataSubResponseDTO.setFields(getFields(v, showFields));
        }

        summaryDataResponseDTO.setSummaryDataId(v.getSummaryData().getId());
        summaryDataResponseDTO.setVersionId(v.getId());
        summaryDataResponseDTO.setUserId(v.getUserUpdateId());
        summaryDataResponseDTO.setSummaryState(v.getState());
        summaryDataResponseDTO.setVersion(v.getVersion());
        summaryDataResponseDTO.setIsActive(v.getIsActive());
        summaryDataResponseDTO.setCreateDate(v.getCreateDate());
        summaryDataResponseDTO.setNumberOfData(getCounts(v.getSummaryData()));
        summaryDataResponseDTO.setType(v.getSummaryData().getType());
        summaryDataResponseDTO.setName(v.getName());
        summaryDataResponseDTO.setCommon(v.getCommon());
        summaryDataResponseDTO.setDescription(v.getDescription());

        return summaryDataResponseDTO;
    }

    private List<? extends SummaryFieldDTO> getFields(SummaryDataVersion v, Boolean showFields) {
        List<SummaryField> summaryFields = v.getSummaryFields();
        Class<? extends SummaryFieldDTO> dtoType = v.getSummaryData().getType().getDtoType();

        List<? extends SummaryFieldDTO> fields = summaryFieldDTOMapper.buildSummaryFieldDTOWithHierarchy(summaryFields)
                .stream()
                .map(f -> convertValue(f, dtoType))
                .toList();

        if (Objects.nonNull(showFields) && showFields) {
            return fields;
        }

        return null;
    }

    private <T, R extends SummaryFieldDTO> R convertValue(T object, Class<R> typeReference) {
        return objectMapper.convertValue(object, typeReference);
    }

    private Long getCounts(SummaryData summaryData) {
        return countDocumentService.countDocumentsById(SummaryDataDTOMapper.USERID, Fields.CONNECTOR_INPUT_ID_FIELD)
                .get(summaryData.getId()
                        .toString());
    }

    private ModelStructFilterDTO modelMapping(SummaryDataVersion v, Boolean showFields) {
        ModelStructFilterDTO modelStructFilterDTO = new ModelStructFilterDTO();
        Long modelStructId = modelStructRepository.findBySummaryDataVersionId(v.getId())
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(v.getId()))
                .getId();
        List<ModelStructComponents> components = modelStructComponentsRepository.findAllByModelStructId(modelStructId);
        List<TransactionCounterDTO> transactionCounterDTOList = transactionCounterService.getAllBySDVModelId(v.getId())
                .values()
                .stream()
                .toList();

        modelStructFilterDTO.setState(getModelState(transactionCounterDTOList));
        if (Objects.isNull(showFields) || Boolean.FALSE.equals(showFields)) {
            List<ModelStructComponentsDTO> componentsDTOS = Collections.emptyList();
            if (!components.isEmpty()) {
                componentsDTOS = getComponentsDTOList(components);
            }
            modelStructFilterDTO.setModelStructComponents(componentsDTOS);
        }

        if (!components.isEmpty()) {
            setAmounts(modelStructFilterDTO);
        }

        return modelStructFilterDTO;
    }

    private static void setAmounts(ModelStructFilterDTO modelStructFilterDTO) {
        int amountOfErrors = modelStructFilterDTO.getModelStructComponents().stream()
                .mapToInt(ModelStructComponentsDTO::getAmountOfErrors)
                .sum();
        int amountOfTransactions = modelStructFilterDTO.getModelStructComponents().stream()
                .filter(f -> f.getType().equals(SummaryDataType.CONNECTOR_INPUT))
                .mapToInt(ModelStructComponentsDTO::getAmountOfTransactions)
                .sum();

        modelStructFilterDTO.setAmountOfTransactions(amountOfTransactions);
        modelStructFilterDTO.setAmountOfErrors(amountOfErrors);
    }

    private List<ModelStructComponentsDTO> getComponentsDTOList(List<ModelStructComponents> components) {
        List<Long> resultList = components.stream()
                .map(v -> v.getSummaryDataVersion()
                        .getUserUpdateId())
                .toList();
        UserListDTO userListDTO = adminServiceClient.getUsers(resultList).getBody();

        if (Objects.isNull(userListDTO) || Objects.isNull(userListDTO.getUserResponseDTOS())) {
            throw new ApplicationException("Users not found", HttpStatus.NOT_FOUND);
        }

        Map<Long, String> longStringMap = userListDTO.getUserResponseDTOS()
                .stream()
                .collect(Collectors.toMap(
                        UserResponseDTO::getId, UserResponseDTO::getCompany, (k, v) -> k, HashMap::new));

        return components.stream()
                .map(c -> getComponentsDTO(c, longStringMap))
                .toList();

    }

    private static TransactionCounterState getModelState(List<TransactionCounterDTO> transactionCounterDTOList) {
        Set<String> states = transactionCounterDTOList.stream().map(TransactionCounterDTO::getState).collect(Collectors.toSet());

        if (Objects.equals(states.size(), 1)) {
            for (TransactionCounterState value : TransactionCounterState.values()) {
                if (states.contains(value.name())) {
                    return value;
                }
            }
        } else {
            return TransactionCounterState.STOP;
        }

        return null;
    }

    private ModelStructComponentsDTO getComponentsDTO(ModelStructComponents m, Map<Long, String> longStringMap) {
        return ModelStructComponentsDTO.builder()
                .modelComponentId(m.getSummaryDataVersion().getId())
                .userName(longStringMap.get(m.getSummaryDataVersion().getUserUpdateId()))
                .name(m.getSummaryDataVersion().getName())
                .summaryState(m.getSummaryDataVersion().getState())
                .state(Objects.nonNull(getTransactionCounterState(m)) ? TransactionCounterState.valueOf(getTransactionCounterState(m)) : null)
                .type(m.getSummaryDataVersion().getSummaryData().getType())
                .subtype(getSubtype(m.getSummaryDataVersion()))
                .launchSecondStage(connectorUtilService.getComponentElement(m, ModelComponentEnumField.LAUNCH_SECOND_STAGE))
                .queueNumber(connectorUtilService.getComponentElement(m, ModelComponentEnumField.QUEUE_NUMBER))
                .test(connectorUtilService.getComponentElement(m, ModelComponentEnumField.TEST))
                .toIncidents(connectorUtilService.getComponentElement(m, ModelComponentEnumField.TO_INCIDENTS))
                .connectorOutputSDVId(connectorUtilService.getComponentElement(m, ModelComponentEnumField.CONNECTOR_OUTPUT_SDV_ID))
                .daysRemaining(m.getDaysRemaining())
                .resultIncremental(m.getResultIncremental())
                .amountOfTransactions(getAmount(m, false))
                .amountOfErrors(getAmount(m, true))
                .build();
    }

    private int getAmount(ModelStructComponents m, Boolean isAmountOfErrors) {
        Long sDVModelId = m.getModelStruct().getSummaryDataVersion().getId();
        Long sDVComponentId = m.getSummaryDataVersion().getId();

        return transactionCounterService.getAmount(sDVModelId, sDVComponentId, isAmountOfErrors);
    }

    private SummarySubType getSubtype(SummaryDataVersion summaryDataVersion) {
        SummaryDataType type = summaryDataVersion.getSummaryData().getType();

        Object object = connectorUtilService.findBySummaryDataVersionIdWithReflection(type, summaryDataVersion.getId());

        if (type.equals(SummaryDataType.DATA_STRUCTURE)) {
            return null;
        }

        try {
            Method getSummaryDataVersionMethod = object.getClass().getMethod(Fields.GET_SUMMARY_SUB_TYPE_FIELD);
            return (SummarySubType) getSummaryDataVersionMethod.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getTransactionCounterState(ModelStructComponents modelStructComponent) {
        Long sDVModelId = modelStructComponent.getModelStruct().getSummaryDataVersion().getId();
        Long modelStructComponentId = modelStructComponent.getSummaryDataVersion().getId();

        TransactionCounterDTO counterDTO = transactionCounterService.getBySDVModelIdAndSDVId(sDVModelId, modelStructComponentId);
        return Objects.nonNull(counterDTO) ? counterDTO.getState() : null;
    }

    private ModelConnectorInputFilterDTO modelConnectorInputMapping(SummaryDataVersion v) {
        ModelConnectorInput modelConnectorInput = modelConnectorInputRepository.findBySummaryDataVersionId(v.getId())
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(v.getId()));
        ModelConnectorInputFilterDTO modelConnectorInputFilterDTO = new ModelConnectorInputFilterDTO();

        modelConnectorInputFilterDTO.setTechnology(modelConnectorInput.getTechnology());
        modelConnectorInputFilterDTO.setDataFormat(modelConnectorInput.getDataFormat());
        modelConnectorInputFilterDTO.setConnectorPurpose(modelConnectorInput.getConnectorPurpose());
        modelConnectorInputFilterDTO.setConnectorSubspecies(modelConnectorInput.getConnectorSubspecies());
        modelConnectorInputFilterDTO.setInputDataType(modelConnectorInput.getInputDataType());
        modelConnectorInputFilterDTO.setConnectorProtocol(modelConnectorInput.getConnectorProtocol());
        modelConnectorInputFilterDTO.setConnectorSubType(modelConnectorInput.getConnectorSubType());

        return modelConnectorInputFilterDTO;
    }

    private List<ModelStructComponentsFilterResponseDTO> getModels(SummaryDataVersion v) {
        return modelStructComponentsRepository.findAllBySummaryDataVersionId(v.getId())
                .stream()
                .map(modelStructComponentsFilterResponseDTOMapper)
                .toList();
    }

    private ModelConnectorOutputFilterDTO modelConnectorOutputMapping(SummaryDataVersion v) {
        ModelConnectorOutput modelConnectorOutput = modelConnectorOutputRepository.findBySummaryDataVersionId(v.getId())
                .orElseThrow(() -> new SummaryDataVersionNotFoundException(v.getId()));
        ModelConnectorOutputFilterDTO modelConnectorOutputFilterDTO = new ModelConnectorOutputFilterDTO();

        modelConnectorOutputFilterDTO.setCheckMainAttributes(modelConnectorOutput.getCheckMainAttributes());
        modelConnectorOutputFilterDTO.setDataFormat(modelConnectorOutput.getDataFormat());
        modelConnectorOutputFilterDTO.setUrl(modelConnectorOutput.getUrl());
        modelConnectorOutputFilterDTO.setConnectorProtocol(modelConnectorOutput.getConnectorProtocol());
        modelConnectorOutputFilterDTO.setConnectorSubType(modelConnectorOutput.getConnectorSubType());

        return modelConnectorOutputFilterDTO;
    }
}