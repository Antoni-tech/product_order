package kz.symtech.antifraud.coreservice.dto.model.struct.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelConnectorInputRequestDTO extends ModelStructComponentsRequestDTO {
    private Boolean launchSecondStage;
}
