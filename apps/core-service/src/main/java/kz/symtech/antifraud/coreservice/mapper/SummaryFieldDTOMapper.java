package kz.symtech.antifraud.coreservice.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import kz.symtech.antifraud.coreservice.dto.filter.response.*;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.utils.JsonToTagMapConverter;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static kz.symtech.antifraud.models.utils.ObjectMapperUtils.objectMapper;

@Service
@RequiredArgsConstructor
public class SummaryFieldDTOMapper {
    private final JsonToTagMapConverter jsonToTagMapConverter;

    public List<? extends SummaryFieldDTO> buildSummaryFieldDTOWithHierarchy(List<SummaryField> fields) {
        List<SummaryField> rootFields = fields
                .stream()
                .sorted(Comparator.comparing(SummaryField::getQueue, Comparator.nullsFirst(Integer::compareTo)))
                .filter(f -> Objects.isNull(f.getParentSummaryField()))
                .toList();
        List<SummaryFieldDTO> rootDTOs = new ArrayList<>();

        for (SummaryField summaryField : rootFields) {
            SummaryFieldDTO summaryFieldDTO = buildSummaryFieldDTO(summaryField);
            rootDTOs.add(summaryFieldDTO);
        }

        return rootDTOs;
    }
    private SummaryFieldDTO buildSummaryFieldDTO(SummaryField summaryField) {
        return buildSummaryFieldDTO(summaryField, false);
    }

    private SummaryFieldDTO buildSummaryFieldDTO(SummaryField summaryField, Boolean miniField) {
        SummaryFieldDTO summaryFieldDTO = new SummaryFieldDTO();
        summaryFieldDTO.setId(summaryField.getId());
        summaryFieldDTO.setName(summaryField.getName());
        summaryFieldDTO.setDefaultField(summaryField.getDefaultField());
        summaryFieldDTO.setFieldType(summaryField.getFieldType());
        summaryFieldDTO.setMaxArray(summaryField.getMaxArray());

        if (Objects.nonNull(summaryField.getTestValueJson())) {
            summaryFieldDTO.setTestValueJson(ObjectMapperUtils.fromStringToJsonNode(summaryField.getTestValueJson()));
        }

        if (miniField) {
            return summaryFieldDTO;
        }

        SummaryDataType type = summaryField.getSummaryDataVersion().getSummaryData().getType();
        return switch (type) {
            case RULE -> buildSummaryRuleFieldDTO(summaryFieldDTO, summaryField);
            case DATA_STRUCTURE -> buildSummaryDataStructureFieldDTO(summaryFieldDTO, summaryField);
            case CONNECTOR_INPUT, CONNECTOR_OUTPUT -> buildSummaryConnectorFieldDTO(summaryFieldDTO, summaryField);
            default -> summaryFieldDTO;
        };
    }

    private SummaryConnectorFieldDTO buildSummaryConnectorFieldDTO(SummaryFieldDTO summaryFieldDTO, SummaryField summaryField) {
        SummaryConnectorFieldDTO summaryConnectorFieldDTO = ObjectMapperUtils.convertValue(summaryFieldDTO, new TypeReference<>() {
        });

        if (Objects.nonNull(summaryField.getSummaryFieldSubData())) {
            summaryConnectorFieldDTO.setMaxSize(summaryField.getSummaryFieldSubData().getMaxSize());
            summaryConnectorFieldDTO.setAllowEmpty(summaryField.getSummaryFieldSubData().getAllowEmpty());
            summaryConnectorFieldDTO.setProhibitSpecCharacters(summaryField.getSummaryFieldSubData().getProhibitSpecCharacters());
            summaryConnectorFieldDTO.setAllowArray(summaryField.getSummaryFieldSubData().getAllowArray());
        }

        return setChildren(summaryConnectorFieldDTO, summaryField);
    }


    private SummaryRuleFieldDTO buildSummaryRuleFieldDTO(SummaryFieldDTO summaryFieldDTO, SummaryField summaryField) {
        SummaryRuleFieldDTO subSummaryFieldDTO = ObjectMapperUtils.convertValue(summaryFieldDTO, new TypeReference<>() {
        });
        return setChildren(subSummaryFieldDTO, summaryField);
    }

    private DataStructureFieldDTO buildSummaryDataStructureFieldDTO(SummaryFieldDTO summaryFieldDTO, SummaryField summaryField) {
        DataStructureFieldDTO subSummaryFieldDTO = ObjectMapperUtils.convertValue(summaryFieldDTO, new TypeReference<>() {
        });
        return setChildren(subSummaryFieldDTO, summaryField);
    }


    private <T extends SummaryFieldDTO> T setChildren(T subSummaryFieldDTO, SummaryField summaryField) {
        if (subSummaryFieldDTO instanceof SummaryRuleFieldDTO summaryRuleFieldDTO) {
            summaryRuleFieldDTO.setChildren(ObjectMapperUtils.convertValue(getChildren(summaryField), new TypeReference<>() {
            }));
        } else if (subSummaryFieldDTO instanceof SummaryConnectorFieldDTO summaryConnectorFieldDTO) {
            summaryConnectorFieldDTO.setChildren(ObjectMapperUtils.convertValue(getChildren(summaryField), new TypeReference<>() {
            }));
        } else if (subSummaryFieldDTO instanceof DataStructureFieldDTO dataStructureFieldDTO) {
            dataStructureFieldDTO.setChildren(ObjectMapperUtils.convertValue(getChildren(summaryField), new TypeReference<>() {
            }));
        }

        return subSummaryFieldDTO;
    }

    private List<? extends SummaryFieldDTO> getChildren(SummaryField summaryField) {
        return getChildren(summaryField, false);
    }

    private List<? extends SummaryFieldDTO> getChildren(SummaryField summaryField, Boolean miniField) {
        List<SummaryField> summaryFieldChildren = summaryField.getSummaryDataVersion().getSummaryFields().stream().filter(v -> v.getParentSummaryField() != null && v.getParentSummaryField().equals(summaryField)).toList();

        if (summaryFieldChildren.isEmpty()) {
            return null;
        } else {
            return summaryFieldChildren.stream()
                    .sorted(Comparator.comparing(SummaryField::getQueue))
                    .map(v -> buildSummaryFieldDTO(v, miniField))
                    .toList();
        }
    }

    public AccessFieldDTO buildAccessFieldDTO(ModelStructComponents component) {
        List<SummaryMiniFieldDTO> fields = Collections.emptyList();
        List<SummaryField> summaryFields = component.getSummaryDataVersion().getSummaryFields();
        if (Objects.nonNull(summaryFields) && !summaryFields.isEmpty()) {
            fields = this.buildSummaryFieldMiniDTOWithHierarchy(summaryFields);
        }
        return AccessFieldDTO.builder()
                .id(component.getSummaryDataVersion().getId())
                .tags(jsonToTagMapConverter.convertJsonToTagMap(fields))
                .type(component.getSummaryDataVersion().getSummaryData().getType())
                .fields(fields)
                .build();
    }

    public List<SummaryMiniFieldDTO> buildSummaryFieldMiniDTOWithHierarchy(List<SummaryField> fields) {
        SummaryDataType type = fields.get(0).getSummaryDataVersion().getSummaryData().getType();

        List<SummaryField> rootFields = fields.stream().filter(f -> Objects.isNull(f.getParentSummaryField())).toList();
        if (type.equals(SummaryDataType.RULE)) {
            return rootFields.stream()
                    .filter(SummaryField::getDefaultField)
                    .map(this::buildSummaryMiniFieldDTO)
                    .toList();
        }
        return rootFields.stream().map(this::buildSummaryMiniFieldDTO).toList();
    }

    private SummaryMiniFieldDTO buildSummaryMiniFieldDTO(SummaryField summaryField) {
        SummaryMiniFieldDTO summaryMiniFieldDTO = objectMapper.convertValue(buildSummaryFieldDTO(summaryField, true), SummaryMiniFieldDTO.class);
        List<SummaryMiniFieldDTO> children = ObjectMapperUtils.convertValue(getChildren(summaryField, true), new TypeReference<>() {
        });

        summaryMiniFieldDTO.setChildren(children);
        return summaryMiniFieldDTO;
    }
}