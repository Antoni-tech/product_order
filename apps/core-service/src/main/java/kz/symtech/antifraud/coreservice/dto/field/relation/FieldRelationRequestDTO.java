package kz.symtech.antifraud.coreservice.dto.field.relation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FieldRelationRequestDTO {
    private Long sDVModelStructId;
    private List<FieldRelationRequestSubDataDTO> fieldRelationRequestSubDataDTOList;
}
