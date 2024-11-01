package kz.symtech.antifraud.coreservice.dto.get.response;

import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsFilterResponseDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryRuleFieldDTO;
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
public class ModelRuleGetResponseDTO {
    private UUID summaryDataId;
    private Long versionId;
    private Long userId;
    private SummaryVersionState state;
    private Short version;
    private Boolean isActive;
    private Date createDate;
    private SummaryDataType type;
    private String name;
    private String description;
    private String userName;
    private SummarySubType summarySubType;
    private Object jsonData;
    private Integer daysRemaining;
    private Boolean resultIncremental;
    private ExtendDataStructureGetResponseDTO extended;
    private List<SummaryRuleFieldDTO> fields;
    private List<ModelStructComponentsFilterResponseDTO> models;
}
