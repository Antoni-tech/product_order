package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.models.dto.model.SummaryFieldCacheDTO;
import kz.symtech.antifraud.models.dto.model.SummaryFieldSubDataDTO;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

@Service
public class SummaryFieldMapper implements Function<SummaryField, SummaryFieldCacheDTO> {
    @Override
    public SummaryFieldCacheDTO apply(SummaryField summaryField) {
        SummaryFieldType type = summaryField.getFieldType();
        SummaryFieldCacheDTO parent = Objects.isNull(summaryField.getParentSummaryField()) ? null : apply(summaryField.getParentSummaryField());
        SummaryFieldSubDataDTO summaryFieldSubDataDTO = Objects.isNull(summaryField.getSummaryFieldSubData()) ? null
                : SummaryFieldSubDataDTO
                .builder()
                .maxSize(summaryField.getSummaryFieldSubData().getMaxSize())
                .allowEmpty(summaryField.getSummaryFieldSubData().getAllowEmpty())
                .allowArray(summaryField.getSummaryFieldSubData().getAllowArray())
                .prohibitSpecCharacters(summaryField.getSummaryFieldSubData().getProhibitSpecCharacters())
                .build();

        return SummaryFieldCacheDTO
                .builder()
                .id(summaryField.getId())
                .summaryDataVersionId(summaryField.getSummaryDataVersion().getId())
                .name(summaryField.getName())
                .type(type)
                .defaultField(summaryField.getDefaultField())
                .parentSummaryField(parent)
                .summaryFieldSubDataDTO(summaryFieldSubDataDTO)
                .build();
    }
}
