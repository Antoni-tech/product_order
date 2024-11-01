package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.coreservice.enums.ConnectorProtocol;
import kz.symtech.antifraud.coreservice.enums.ConnectorSubType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModelConnectorOutputFilterDTO extends SummaryDataSubResponseDTO{
    private Boolean checkMainAttributes;
    private String dataFormat;
    private Integer daysRemaining;
    private String url;
    private ConnectorProtocol connectorProtocol;
    private ConnectorSubType connectorSubType;
}
