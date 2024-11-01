package kz.symtech.antifraud.connectorhandlerservice.controllers;

import kz.symtech.antifraud.connectorhandlerservice.dto.IncidentListResponseDTO;
import kz.symtech.antifraud.connectorhandlerservice.services.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping("/all")
    public IncidentListResponseDTO getAll(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam Long modelId, @RequestParam Long userId) {
        return incidentService.getAll(page, size, modelId, userId);
    }
}
