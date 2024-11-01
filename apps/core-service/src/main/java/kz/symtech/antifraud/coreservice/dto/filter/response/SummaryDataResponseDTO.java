package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SummaryDataResponseDTO {
    private UUID summaryDataId;
    private Long versionId;
    private Long userId;
    private String userName;
    private SummaryVersionState summaryState;
    private short version;
    private Boolean isActive;
    private Date createDate;
    private Long numberOfData;
    private SummaryDataType type;
    private String name;
    private String description;
    private Boolean common;
}