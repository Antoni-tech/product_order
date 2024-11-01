package kz.symtech.antifraud.feignclients.clients.services;

import kz.symtech.antifraud.models.dto.IncidentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "test-service")
public interface TestServiceClient {

    @PostMapping("test-service/api/v1/incidents")
    void handleIncident(@RequestBody IncidentResponseDTO incidentResponseDTO);

    @PostMapping("test-service/api/v1/incidents/test-url")
    String handleRequest(@RequestBody String jsonData);
}
