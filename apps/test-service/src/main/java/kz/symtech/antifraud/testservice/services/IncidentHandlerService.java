package kz.symtech.antifraud.testservice.services;

import kz.symtech.antifraud.models.dto.IncidentResponseDTO;

public interface IncidentHandlerService {
    void handle(IncidentResponseDTO incidentResponseDTO);
}
