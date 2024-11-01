package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.coreservice.enums.ConnectorProtocol;
import kz.symtech.antifraud.coreservice.enums.ConnectorSubType;
import kz.symtech.antifraud.coreservice.enums.InputDataType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelConnectorInputFilterDTO extends SummaryDataSubResponseDTO{
    private String connectorPurpose;
    private String connectorSubspecies;
    private String technology;
    private Boolean launchSecondStage;
    private String dataFormat;
    private int daysRemaining;
    private InputDataType inputDataType;
    private ConnectorProtocol connectorProtocol;
    private ConnectorSubType connectorSubType;
}
