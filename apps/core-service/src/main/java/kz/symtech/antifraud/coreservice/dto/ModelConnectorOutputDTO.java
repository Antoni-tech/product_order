package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModelConnectorOutputDTO {
    private Long id;
    private SummaryDataType type;
    private String name;
    private Long createUserId;
    private Boolean checkMainAttributes;
    private String dataFormat;
    private int daysRemaining;
    private String url;
}
