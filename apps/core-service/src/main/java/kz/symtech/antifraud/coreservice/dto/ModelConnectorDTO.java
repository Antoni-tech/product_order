package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelConnectorDTO {
    private Long id;
    private Long userName;
    private String name;
    private SummaryDataType type;
    private SummarySubType summarySubType;
    private String dataFormat;
    private Boolean common;
}
