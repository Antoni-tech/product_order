package kz.symtech.antifraud.coreservice.dto.filter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModelStructComponentsFilterResponseDTO {
    private Long modelId;
    private String modelName;
    private Integer daysRemaining;
    private int queueNumber;
    private Boolean resultIncremental;
    private Boolean launchSecondStage;
}