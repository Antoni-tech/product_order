package kz.symtech.antifraud.coreservice.dto.field.relation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FieldRelationRequestSubDataDTO {
    private Long varSummaryFieldId;
    private Long srcSummaryFieldId;
}
