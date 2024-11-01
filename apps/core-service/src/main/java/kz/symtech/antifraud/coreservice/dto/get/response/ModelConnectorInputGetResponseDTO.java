package kz.symtech.antifraud.coreservice.dto.get.response;

import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsFilterResponseDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryConnectorFieldDTO;
import kz.symtech.antifraud.coreservice.enums.*;
import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ModelConnectorInputGetResponseDTO {
    private UUID summaryDataId;
    private Long versionId;
    private Long userId;
    private SummaryVersionState state;
    private short version;
    private Boolean isActive;
    private Date createDate;
    private SummaryDataType type;
    private String name;
    private String description;
    private String userName;
    private String connectorPurpose;
    private Boolean launchSecondStage;
    private String connectorSubspecies;
    private String technology;
    private String dataFormat;
    private String urlForInfo;
    private String cron;
    private int daysRemaining;
    private InputDataType inputDataType;
    private SummarySubType summarySubType;
    private ConnectorProtocol connectorProtocol;
    private ConnectorSubType connectorSubType;
    private ExtendDataStructureGetResponseDTO extended;
    private List<ModelStructComponentsFilterResponseDTO> models;
    private List<SummaryConnectorFieldDTO> fields;
}
