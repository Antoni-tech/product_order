package kz.symtech.antifraud.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IncidentResponseDTO {
    private Long sDVModelId;
    private Long userId;
    private Long sDVConnectorId;
    private int number;
    private String textValue;
    private String jsonData;
    private Long sDVConnectorOutputId;
}
