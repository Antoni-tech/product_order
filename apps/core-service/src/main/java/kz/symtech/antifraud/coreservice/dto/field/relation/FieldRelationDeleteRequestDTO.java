package kz.symtech.antifraud.coreservice.dto.field.relation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FieldRelationDeleteRequestDTO {
    private Long sDVModelId;
    private Long sDVRuleId;
    private List<Long> fieldRelationIds;
}
