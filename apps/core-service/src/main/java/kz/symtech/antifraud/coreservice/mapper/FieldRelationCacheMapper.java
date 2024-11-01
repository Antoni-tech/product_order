package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.enums.SummaryType;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FieldRelationCacheMapper implements Function<FieldRelation, FieldRelationDTO> {
    @Override
    public FieldRelationDTO apply(FieldRelation fieldRelation) {
        String fieldNameRule = fieldRelation.getVarSummaryField().getName();
        String fieldNameConnector = fieldRelation.getSrcSummaryField().getName();
        Long modelRuleSDVId = fieldRelation.getVarSummaryField().getSummaryDataVersion().getId();
        Long summaryFieldRuleId = fieldRelation.getVarSummaryField().getId();
        Long modelConnectorInputId = fieldRelation.getSrcSummaryField().getSummaryDataVersion().getId();
        SummaryFieldType type = fieldRelation.getSrcSummaryField().getFieldType();
        SummaryDataType sourceType = fieldRelation.getSrcSummaryField().getSummaryDataVersion().getSummaryData().getType();

        return FieldRelationDTO
                .builder()
                .fieldNameVar(fieldNameRule)
                .fieldNameSrc(fieldNameConnector)
                .defaultField(fieldRelation.getSrcSummaryField().getDefaultField())
                .type(type)
                .summaryDataVersionId(modelRuleSDVId)
                .summaryFieldId(summaryFieldRuleId)
                .sDVSourceId(modelConnectorInputId)
                .sourceId(fieldRelation.getSrcSummaryField().getId())
                .sourceType(SummaryType.valueOf(sourceType.name()))
                .summaryDataVersionModelId(fieldRelation.getModelStruct().getSummaryDataVersion().getId())
                .build();
    }
}
