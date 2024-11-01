package kz.symtech.antifraud.coreservice.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.symtech.antifraud.coreservice.dto.filter.response.DataStructureFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryConnectorFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryRuleFieldDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryFieldSubData;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.exceptions.ApplicationException;
import kz.symtech.antifraud.coreservice.repository.FieldRelationRepository;
import kz.symtech.antifraud.coreservice.repository.ModelStructComponentsRepository;
import kz.symtech.antifraud.coreservice.repository.SummaryFieldRepository;
import kz.symtech.antifraud.coreservice.repository.SummaryFieldSubDataRepository;
import kz.symtech.antifraud.coreservice.services.ConnectorUtilService;
import kz.symtech.antifraud.coreservice.services.SummaryFieldService;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.enums.SummarySubType;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryFieldServiceImpl implements SummaryFieldService {

    private final SummaryFieldRepository summaryFieldRepository;
    private final ObjectMapper objectMapper;
    private final SummaryFieldSubDataRepository summaryFieldSubDataRepository;
    private final FieldRelationRepository fieldRelationRepository;
    private final ModelStructComponentsRepository modelStructComponentsRepository;
    private final ConnectorUtilService connectorUtilService;

    @Override
    public SummaryField getEntity(Long id) {
        return summaryFieldRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Summary field not found"));
    }

    @Override
    @Transactional
    public List<SummaryField> createFields(SummaryDataVersion summaryDataVersion, Map<String, Object> map) {
        List<SummaryField> fieldList = new ArrayList<>();

        List<? extends SummaryFieldDTO> fieldDTOList = convertMapToDTO(map, summaryDataVersion);
        processHierarchy(fieldList, fieldDTOList, null, summaryDataVersion);

        deleteNotExistingFields(summaryDataVersion, fieldList, map);
        getDefaultRuleField(summaryDataVersion, map, fieldList);

        setQueue(fieldList);

        return summaryFieldRepository.saveAll(fieldList);
    }

    private static void setQueue(List<SummaryField> fieldList) {
        IntStream.range(0, fieldList.size())
                .forEach(index -> fieldList.get(index).setQueue(index + 1));
    }

    private void getDefaultRuleField(SummaryDataVersion summaryDataVersion, Map<String, Object> map, List<SummaryField> fieldList) {
        boolean rule = summaryDataVersion.getSummaryData().getType().equals(SummaryDataType.RULE);
        boolean isCreate = Boolean.parseBoolean(String.valueOf(map.get(Fields.IS_CREATE_FIELD)));

        if (rule && isCreate) {
            String ruleType = (String) map.get(Fields.SUMMARY_SUB_TYPE_FIELD);
            boolean quantityType = SummarySubType.valueOf(ruleType).equals(SummarySubType.QUANTITY);

            if (quantityType) {
                SummaryField summaryField = new SummaryField();
                summaryField.setSummaryDataVersion(summaryDataVersion);
                summaryField.setName(Fields.RESULT_FIELD);
                summaryField.setDefaultField(true);
                fieldList.add(summaryField);
            }
        }
    }

    private List<? extends SummaryFieldDTO> convertMapToDTO(Map<String, Object> map, SummaryDataVersion summaryDataVersion) {
        List<Object> fields = ObjectMapperUtils.convertValue(map.get(Fields.FIELDS_KEY), new TypeReference<>() {
        });
        SummaryDataType type = summaryDataVersion.getSummaryData().getType();

        return fields.stream().map(f -> objectMapper.convertValue(f, type.getDtoType())).toList();
    }

    private void processHierarchy(List<SummaryField> fieldList, List<? extends SummaryFieldDTO> fieldDTOList, SummaryField parentField, SummaryDataVersion summaryDataVersion) {
        for (SummaryFieldDTO summaryFieldDTO : fieldDTOList) {
            validateName(fieldDTOList);

            SummaryField summaryField = buildSummaryField(summaryFieldDTO, parentField, summaryDataVersion);
            List<? extends SummaryFieldDTO> children = null;
            if (Objects.nonNull(summaryField)) {
                children = getInstanceOf(summaryFieldDTO, summaryField);
                summaryFieldRepository.save(summaryField);
                fieldList.add(summaryField);
            }

            if (Objects.nonNull(children) && !children.isEmpty()) {
                processHierarchy(fieldList, children, summaryField, summaryDataVersion);
            }
        }
    }

    private SummaryField buildSummaryField(SummaryFieldDTO summaryFieldDTO, SummaryField parentField, SummaryDataVersion summaryDataVersion) {
        SummaryField summaryField = null;

        Long fieldId = summaryFieldDTO.getId();
        if (Objects.nonNull(fieldId) && fieldId == -1) {
            summaryField = new SummaryField();
        } else {
            if (Objects.nonNull(summaryDataVersion.getSummaryFields())) {
                summaryField = summaryDataVersion.getSummaryFields().stream()
                        .filter(v -> Objects.equals(v.getId(), fieldId))
                        .findFirst()
                        .orElse(null);
            }
        }

        if (Objects.nonNull(summaryField)) {
            summaryField.setSummaryDataVersion(summaryDataVersion);
            summaryField.setParentSummaryField(parentField);
            summaryField.setName(summaryFieldDTO.getName().trim());
            if (Objects.nonNull(summaryFieldDTO.getDefaultField())) {
                summaryField.setDefaultField(summaryFieldDTO.getDefaultField());
            }
            summaryField.setFieldType(summaryFieldDTO.getFieldType());
            summaryField.setMaxArray(summaryFieldDTO.getMaxArray());

            if (Objects.nonNull(summaryFieldDTO.getTestValueJson())) {
                summaryField.setTestValueJson(ObjectMapperUtils.getJson(summaryFieldDTO.getTestValueJson()));
            }
        }

        return summaryField;
    }

    private List<? extends SummaryFieldDTO> getInstanceOf(SummaryFieldDTO summaryFieldDTO, SummaryField summaryField) {
        if (summaryFieldDTO instanceof SummaryConnectorFieldDTO summaryConnectorFieldDTO) {
            buildSubData(summaryConnectorFieldDTO, summaryField);
            return summaryConnectorFieldDTO.getChildren();
        } else if (summaryFieldDTO instanceof SummaryRuleFieldDTO summaryRuleFieldDTO) {
            return summaryRuleFieldDTO.getChildren();
        } else if (summaryFieldDTO instanceof DataStructureFieldDTO dataStructureFieldDTO) {
            return dataStructureFieldDTO.getChildren();
        } else {
            throw new ApplicationException("There is no instanceof found", HttpStatus.BAD_REQUEST);
        }
    }

    private void buildSubData(SummaryConnectorFieldDTO summaryConnectorFieldDTO, SummaryField summaryField) {
        if (!summaryField.getFieldType().equals(SummaryFieldType.OBJECT) && !summaryField.getFieldType().equals(SummaryFieldType.ARRAY_OBJECT)) {
            SummaryFieldSubData summaryFieldSubData = Objects.nonNull(summaryField.getSummaryFieldSubData()) ? summaryField.getSummaryFieldSubData() : new SummaryFieldSubData();
            connectorUtilService.getDeclaredFields(summaryFieldSubData, ObjectMapperUtils.convertValue(summaryConnectorFieldDTO, new TypeReference<>() {
            }));
            summaryFieldSubDataRepository.save(summaryFieldSubData);
            summaryFieldSubData.setSummaryField(summaryField);
        }
    }

    private void validateName(List<? extends SummaryFieldDTO> listByParent) {
        Set<String> existingFieldNames = new HashSet<>();

        listByParent.forEach(fieldDTO -> {
            String name = fieldDTO.getName();
            if (!existingFieldNames.add(name)) {
                throw new ApplicationException("Field should have unique name: " + name, HttpStatus.BAD_REQUEST);
            }
        });
    }

    private void deleteNotExistingFields(SummaryDataVersion summaryDataVersion, List<SummaryField> newFields, Map<String, Object> map) {
        if (Boolean.FALSE.equals(map.get(Fields.IS_CREATE_FIELD))) {
            List<SummaryField> oldFields = summaryDataVersion.getSummaryFields();
            List<SummaryField> deleteFields = oldFields.stream()
                    .filter(f -> newFields.stream()
                            .noneMatch(v -> Objects.equals(v.getId(), f.getId()))
                            && !f.getDefaultField())
                    .toList();

            checkForFieldRelation(summaryDataVersion.getId(), deleteFields);
            deleteSummaryFields(deleteFields);
        }
    }

    public void deleteSummaryFields(List<SummaryField> deleteFields) {
        summaryFieldRepository.deleteAll(deleteFields);
    }

    public void checkForFieldRelation(Long versionId, List<SummaryField> deleteFields) {
        List<ModelStructComponents> components = modelStructComponentsRepository.findAllBySummaryDataVersionId(versionId);

        if (!components.isEmpty() && !deleteFields.isEmpty()) {
            List<FieldRelation> varFields = fieldRelationRepository.findAllByVarSummaryFieldIn(deleteFields);
            List<FieldRelation> srcFields = fieldRelationRepository.findAllBySrcSummaryFieldIn(deleteFields);

            throwDoNotDeleteException(components, varFields, srcFields);
        }
    }

    public void throwDoNotDeleteException(List<ModelStructComponents> components, List<FieldRelation> varFields, List<FieldRelation> srcFields) {
        StringBuilder errorMessage = new StringBuilder("You can't delete field because of usage in model(s): ");
        String errorFields = components.stream()
                .map(v ->  v.getId().toString())
                .collect(Collectors.joining(", "));
        errorMessage.append(errorFields).append(";");

        if (!varFields.isEmpty()) {
            String errorVarFields = varFields.stream()
                    .map(v -> v.getId().toString())
                    .collect(Collectors.joining(", "));
            errorMessage.append(" Field relation with var: ").append(errorVarFields).append(";");
        }

        if (!srcFields.isEmpty()) {
            String errorSrcFields = srcFields.stream()
                    .map(v -> v.getId().toString())
                    .collect(Collectors.joining(", "));
            errorMessage.append(" Field relation with src: ").append(errorSrcFields).append(";");
        }

        throw new ApplicationException(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    public List<SummaryField> duplicateFields(SummaryDataVersion summaryDataVersion, SummaryDataVersion duplicateSummaryDataVersion) {
        List<SummaryField> summaryFields = summaryFieldRepository.findAllBySummaryDataVersionId(summaryDataVersion.getId());
        Map<SummaryField, List<SummaryField>> mapByParent = processFieldsByParent(summaryFields);

        List<SummaryField> duplicatedFields = new ArrayList<>();
        getListOfDuplicatedFields(mapByParent.get(null), null, duplicateSummaryDataVersion, duplicatedFields);

        return duplicatedFields;
    }

    private Map<SummaryField, List<SummaryField>> processFieldsByParent(List<SummaryField> list) {
        Set<SummaryField> parentFields = list.stream()
                .map(SummaryField::getParentSummaryField).collect(Collectors.toSet());
        Map<SummaryField, List<SummaryField>> mapByParent = new HashMap<>();

        for (SummaryField parent : parentFields) {
            if (Objects.nonNull(parent)) {
                mapByParent.put(parent, list.stream().filter(v -> v.getParentSummaryField() != null && v.getParentSummaryField().equals(parent)).toList());
            } else {
                mapByParent.put(null, list.stream().filter(v -> v.getParentSummaryField() == null).toList());
            }
        }
        return mapByParent;
    }

    private void getListOfDuplicatedFields(List<SummaryField> listByParent, SummaryField newParent, SummaryDataVersion duplicateSummaryDataVersion, List<SummaryField> duplicatedFields) {
        for (SummaryField summaryField : listByParent) {
            SummaryField duplicatedField = new SummaryField();
            summaryField.copyTo(duplicatedField);
            duplicatedField.setSummaryDataVersion(duplicateSummaryDataVersion);
            duplicatedField.setParentSummaryField(newParent);
            duplicatedField.setSummaryFieldSubData(buildSubData(summaryField, duplicatedField));

            summaryFieldRepository.save(duplicatedField);
            duplicatedFields.add(duplicatedField);

            List<SummaryField> childList = summaryField.getSummaryDataVersion().getSummaryFields().stream().filter(v -> Objects.nonNull(v.getParentSummaryField()) && v.getParentSummaryField().equals(summaryField)).toList();

            if (!childList.isEmpty()) {
                getListOfDuplicatedFields(childList, duplicatedField, duplicateSummaryDataVersion, duplicatedFields);
            }
        }
    }

    private SummaryFieldSubData buildSubData(SummaryField summaryField, SummaryField duplicateSummaryField) {
        if (Objects.nonNull(summaryField.getSummaryFieldSubData())) {
            SummaryFieldSubData duplicateSummaryFieldSubData = new SummaryFieldSubData();
            summaryField.getSummaryFieldSubData().copyTo(duplicateSummaryFieldSubData);
            summaryFieldSubDataRepository.save(duplicateSummaryFieldSubData);
            duplicateSummaryFieldSubData.setSummaryField(duplicateSummaryField);
            return duplicateSummaryFieldSubData;
        }
        return null;
    }
}