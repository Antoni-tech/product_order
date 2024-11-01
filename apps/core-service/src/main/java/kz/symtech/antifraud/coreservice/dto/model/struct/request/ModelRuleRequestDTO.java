package kz.symtech.antifraud.coreservice.dto.model.struct.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelRuleRequestDTO extends ModelStructComponentsRequestDTO {
    private Integer queueNumber;
    private Long connectorOutputSDVId;
    private Boolean toIncidents;
}