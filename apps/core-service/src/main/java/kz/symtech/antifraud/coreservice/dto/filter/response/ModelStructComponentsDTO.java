package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModelStructComponentsDTO {
    private Long modelComponentId;
    private String userName;
    private String name;
    private SummaryDataType type;
    private SummaryVersionState summaryState;
    private TransactionCounterState state;
    private SummarySubType subtype;
    private Boolean launchSecondStage;
    private Integer queueNumber;
    private Integer daysRemaining;
    private Boolean resultIncremental;
    private Boolean toIncidents;
    private Boolean test;
    private Long connectorOutputSDVId;
    private int amountOfTransactions;
    private int amountOfErrors;
}
