package kz.symtech.antifraud.testservice.controllers;

import kz.symtech.antifraud.models.dto.IncidentResponseDTO;
import kz.symtech.antifraud.testservice.services.IncidentHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/incidents")
@Slf4j
public class IncidentController {

    private final IncidentHandlerService incidentHandlerService;

    @PostMapping
    public void handleIncident(@RequestBody IncidentResponseDTO incidentResponseDTO) {
        incidentHandlerService.handle(incidentResponseDTO);
    }

    @PostMapping("/test-url")
    public String handleRequest(@RequestBody String jsonData) {
        log.info("Test input data {}",jsonData);
        return jsonData;
    }
}
