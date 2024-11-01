package kz.symtech.antifraud.testservice.services.impl;

import kz.symtech.antifraud.models.dto.IncidentResponseDTO;
import kz.symtech.antifraud.testservice.entities.Incident;
import kz.symtech.antifraud.testservice.mappers.IncidentMapper;
import kz.symtech.antifraud.testservice.repositories.IncidentRepository;
import kz.symtech.antifraud.testservice.services.IncidentHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentHandlerServiceImpl implements IncidentHandlerService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    @Override
    public void handle(IncidentResponseDTO incidentResponseDTO) {
        Incident incident = incidentMapper.apply(incidentResponseDTO);
        incidentRepository.save(incident);
    }
}
