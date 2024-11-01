package kz.symtech.antifraud.coreservice.dto.field.relation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FieldRelationResponseDTO {
    private Long fieldRelationId;
    private Long varSummaryFieldId;
    private String varName;
    private Long srcSummaryFieldId;
    private String srcName;
}
