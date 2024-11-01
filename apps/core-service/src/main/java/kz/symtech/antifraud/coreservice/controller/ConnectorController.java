package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.SummaryDataFilterRequestDTO;
import kz.symtech.antifraud.coreservice.dto.SummaryDataListResponseDTO;
import kz.symtech.antifraud.coreservice.dto.ModelConnectorDTO;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.services.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connector")
public class ConnectorController {
    private final ModelService modelService;

    @PostMapping("/create-or-update")
    public ResponseEntity<Long> createInputConnector(@RequestBody Object object) {
        return ResponseEntity.ok(modelService.createConnector(object));
    }

    @PostMapping("/all")
    public ResponseEntity<SummaryDataListResponseDTO> getConnectorsFilteredByCreateDate(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam Long userId,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestBody SummaryDataFilterRequestDTO summaryDataFilterRequestDTO) {
        List<SummaryDataType> types = List.of(SummaryDataType.CONNECTOR_INPUT, SummaryDataType.CONNECTOR_OUTPUT);

        return ResponseEntity.ok(modelService.filter(summaryDataFilterRequestDTO, page, size, userId, orderBy, types));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ModelConnectorDTO>> getAll() {
        return ResponseEntity.ok(modelService.getAllConnectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.getConnector(id));
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Long> duplicate(@RequestParam Long id) {
        return ResponseEntity.ok(modelService.duplicateConnector(id));
    }

    @GetMapping("/getUrl/{id}")
    public String getUrl(@PathVariable Long id) {
        return modelService.getUrlConnectorOutput(id);
    }
}
