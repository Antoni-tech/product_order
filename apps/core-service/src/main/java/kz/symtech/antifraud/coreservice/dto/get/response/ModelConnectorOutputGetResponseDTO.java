package kz.symtech.antifraud.coreservice.dto.get.response;

import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsFilterResponseDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryConnectorFieldDTO;
import kz.symtech.antifraud.coreservice.enums.ConnectorProtocol;
import kz.symtech.antifraud.coreservice.enums.ConnectorSubType;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ModelConnectorOutputGetResponseDTO {
    private UUID summaryDataId;
    private Long versionId;
    private Long userId;
    private SummaryVersionState state;
    private short version;
    private Boolean isActive;
    private Date createDate;
    private Long numberOfData;
    private SummaryDataType type;
    private String name;
    private String description;
    private String userName;
    private Boolean checkMainAttributes;
    private String dataFormat;
    private String urlForInfo;
    private String cron;
    private int daysRemaining;
    private String url;
    private SummarySubType summarySubType;
    private ConnectorProtocol connectorProtocol;
    private ConnectorSubType connectorSubType;
    private ExtendDataStructureGetResponseDTO extended;
    private List<ModelStructComponentsFilterResponseDTO> models;
    private List<SummaryConnectorFieldDTO> fields;
}
