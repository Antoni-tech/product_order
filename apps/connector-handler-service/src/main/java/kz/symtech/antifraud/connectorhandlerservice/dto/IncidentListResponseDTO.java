package kz.symtech.antifraud.connectorhandlerservice.dto;

import kz.symtech.antifraud.connectorhandlerservice.entities.Incident;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class IncidentListResponseDTO {
    private Long totalCount;
    private List<Incident> incidents;
}
