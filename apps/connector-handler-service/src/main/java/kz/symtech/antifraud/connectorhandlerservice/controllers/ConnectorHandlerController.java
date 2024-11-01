package kz.symtech.antifraud.connectorhandlerservice.controllers;

import kz.symtech.antifraud.connectorhandlerservice.services.ConnectorInputHandlerService;
import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/connector-handler")
public class ConnectorHandlerController {

    private final ConnectorInputHandlerService connectorInputHandlerService;

    @PostMapping("/{summaryDataIdModel}/{summaryDataIdConnector}/{userId}")
    public Set<String> add(
            @PathVariable UUID summaryDataIdModel,
            @PathVariable UUID summaryDataIdConnector,
            @PathVariable Long userId,
            @RequestBody Map<String, Object> data) {
        return connectorInputHandlerService.handleConnectorInput(summaryDataIdModel, summaryDataIdConnector, data, userId);
    }

    @PostMapping("state/{summaryDataIdModel}")
    void changeState(@PathVariable UUID summaryDataIdModel, @RequestParam TransactionCounterState state) {
        connectorInputHandlerService.changeState(summaryDataIdModel, state);
    }

    @PostMapping("model/{summaryDataIdModel}")
    void changeModel(@PathVariable UUID summaryDataIdModel, @RequestBody ModelStructResponseDTO modelStructResponseDTO) {
        connectorInputHandlerService.changeModel(summaryDataIdModel, modelStructResponseDTO);
    }

}
