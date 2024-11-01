package kz.symtech.antifraud.connectorhandlerservice.services;

import kz.symtech.antifraud.connectorhandlerservice.dto.IncidentListResponseDTO;
import kz.symtech.antifraud.connectorhandlerservice.entities.Incident;

public interface IncidentService {
    void save(Incident incident);
    IncidentListResponseDTO getAll(Long page, Long size, Long modelId, Long userId);
}
