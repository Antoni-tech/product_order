package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.enums.InputDataType;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ModelConnectorInputDTO {
    private Long id;
    private SummaryDataType type;
    private String name;
    private Long createUserId;
    private Boolean launchSecondStage;
    private String technology;
    private String dataFormat;
    private InputDataType inputDataType;
    private int daysRemaining;
}