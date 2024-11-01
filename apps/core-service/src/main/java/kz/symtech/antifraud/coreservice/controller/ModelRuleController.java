package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.ModelRuleDTO;
import kz.symtech.antifraud.coreservice.dto.ModelRuleCreateRequestDTO;
import kz.symtech.antifraud.coreservice.dto.SummaryDataFilterRequestDTO;
import kz.symtech.antifraud.coreservice.dto.SummaryDataListResponseDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelRuleGetResponseDTO;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.services.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rule")
public class ModelRuleController {
    private final ModelService modelService;

    @PostMapping("/create-or-update")
    public ResponseEntity<Long> createModelRule(@RequestBody ModelRuleCreateRequestDTO modelRuleCreateRequestDTO) {
        return ResponseEntity.ok(modelService.createModelRule(modelRuleCreateRequestDTO));
    }

    @PostMapping("/all")
    public ResponseEntity<SummaryDataListResponseDTO> getConnectorsFilteredByCreateDate(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam Long userId,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestBody SummaryDataFilterRequestDTO summaryDataFilterRequestDTO) {
        return ResponseEntity.ok(modelService.filter(summaryDataFilterRequestDTO, page, size, userId, orderBy, Collections.singletonList(SummaryDataType.RULE)));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ModelRuleDTO>> getAll() {
        return ResponseEntity.ok(modelService.getModelRules());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ModelRuleGetResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.getModelRule(id, null));
    }

    @GetMapping("/{id}/{modelId}")
    public ResponseEntity<ModelRuleGetResponseDTO> getForModel(@PathVariable Long id, @PathVariable(value = "modelId", required = false) Long modelId) {
        return ResponseEntity.ok(modelService.getModelRule(id, modelId));
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Long> duplicate(@RequestParam Long id) {
        return ResponseEntity.ok(modelService.duplicateRule(id));
    }

}
