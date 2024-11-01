package kz.symtech.antifraud.models.dto.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityOperationDTO implements Serializable {
    private Long summaryDataVersionConnectorOutputId;
    private String textValue;
    private Boolean toIncidents;
    private int number;
}
