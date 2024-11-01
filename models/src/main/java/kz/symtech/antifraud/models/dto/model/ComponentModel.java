package kz.symtech.antifraud.models.dto.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComponentModel implements Serializable {
    private Long summaryDataVersionId;
    private UUID summaryDataId;
    private int queueNumber;
    private Boolean launchSecondStage;
    private int daysRemaining;
    private Boolean saveResult;
    private Boolean resultIncremental;
    private Boolean test;
    private Boolean isActive;
    private Boolean validateFields;
    private List<SummaryFieldCacheDTO> summaryFields;
    private List<ComponentElementDTO> elements;
}
