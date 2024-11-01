package kz.symtech.antifraud.testservice.mappers;

import kz.symtech.antifraud.models.dto.IncidentResponseDTO;
import kz.symtech.antifraud.testservice.entities.Incident;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IncidentMapper implements Function<IncidentResponseDTO, Incident> {
    @Override
    public Incident apply(IncidentResponseDTO incidentResponseDTO) {
        Incident i = new Incident();
        i.setNumber(incidentResponseDTO.getNumber());
        i.setTextValue(incidentResponseDTO.getTextValue());
        i.setJsonData(incidentResponseDTO.getJsonData());
        i.setUserId(incidentResponseDTO.getUserId());
        i.setSummaryDataVersionModelId(incidentResponseDTO.getSDVModelId());
        i.setSummaryDataVersionConnectorId(incidentResponseDTO.getSDVConnectorId());
        i.setSummaryDataVersionConnectorOutputId(incidentResponseDTO.getSDVConnectorOutputId());

        return i;
    }
}
