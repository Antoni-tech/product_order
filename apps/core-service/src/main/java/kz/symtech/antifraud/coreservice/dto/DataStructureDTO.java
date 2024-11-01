package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DataStructureDTO {
    private Long id;
    private SummaryDataType type;
    private String name;
    private String description;
    private Long createUserId;
}