package kz.symtech.antifraud.coreservice.dto.field.relation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FieldRelationUpdateRequestDTO {
    private Long modelId;
    private Long fieldRelationId;
    private FieldRelationRequestSubDataDTO fieldRelationRequestSubDataDTO;
}
