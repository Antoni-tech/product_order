package kz.symtech.antifraud.coreservice.dto.model.struct.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelConnectorOutputRequestDTO extends ModelStructComponentsRequestDTO {
    private Boolean test;
//    private Boolean launchSecondStage;
}