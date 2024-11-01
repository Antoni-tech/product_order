package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryDataResponseDTO;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import kz.symtech.antifraud.coreservice.services.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
public class SummaryDataController {

    private final ModelService modelService;

    @PostMapping("/change-state")
    public ResponseEntity<SummaryVersionState> publishConnector(@RequestParam Long id, @RequestParam SummaryVersionState state) {
        return ResponseEntity.ok(modelService.publishOrCancel(id, state));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDraftOrCanceled(@RequestParam Long id) {
        modelService.deleteDraftOrCanceled(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-active")
    public ResponseEntity<Boolean> changeActiveVersion(@RequestParam Long id) {
        return ResponseEntity.ok(modelService.changeActiveVersion(id));
    }

    @GetMapping("/history")
    public ResponseEntity<List<SummaryDataResponseDTO>> history(@RequestParam Long id) {
        return ResponseEntity.ok(modelService.historyOfChanges(id));
    }

    @GetMapping("/access-field/{sdvModelId}/{sdvRuleId}")
    public ResponseEntity<Object> getAccsesFields(@PathVariable Long sdvModelId, @PathVariable Long sdvRuleId) {
        return ResponseEntity.ok(modelService.getAccessFields(sdvModelId, sdvRuleId));
    }
}