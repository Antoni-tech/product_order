package kz.symtech.antifraud.coreservice.dto.model.struct.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ModelStructComponentsRequestDTO {
    private Long componentId;
    private Integer daysRemaining;
    private Boolean resultIncremental = false;
}