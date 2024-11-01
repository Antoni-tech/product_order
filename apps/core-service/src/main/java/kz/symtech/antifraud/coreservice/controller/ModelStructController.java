package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.SummaryDataFilterRequestDTO;
import kz.symtech.antifraud.coreservice.dto.SummaryDataListResponseDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryDataResponseDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelStructGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.model.struct.request.ModelStructRequestDTO;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.services.ModelResponseBuildService;
import kz.symtech.antifraud.coreservice.services.ModelService;
import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/model")
public class ModelStructController {

    private final ModelService modelService;
    private final ModelResponseBuildService modelResponseBuildService;

    @PostMapping("/create-or-update")
    public ResponseEntity<Long> createModelStruct(@RequestBody ModelStructRequestDTO modelStructRequestDTO) {
        return ResponseEntity.ok(modelService.createModelStruct(modelStructRequestDTO));
    }

    @PostMapping("/all")
    public ResponseEntity<SummaryDataListResponseDTO> getConnectorsFilteredByCreateDate(@RequestParam(value = "page", required = false) Long page,
                                                                                        @RequestParam(value = "size", required = false) Long size,
                                                                                        @RequestParam Long userId,
                                                                                        @RequestParam(value = "orderBy", required = false) String orderBy,
                                                                                        @RequestBody SummaryDataFilterRequestDTO summaryDataFilterRequestDTO) {
        return ResponseEntity.ok(modelService.filter(summaryDataFilterRequestDTO, page, size, userId, orderBy,  Collections.singletonList(SummaryDataType.MODEL)));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ModelStructGetResponseDTO>> getAll() {
        return ResponseEntity.ok(modelService.getModels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SummaryDataResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.getModel(id));
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Long> duplicate(@RequestParam Long id){
        return ResponseEntity.ok(modelService.duplicateModel(id));
    }

    @GetMapping("/info/{summaryDataId}")
    public ModelStructResponseDTO getModelInfo(@PathVariable UUID summaryDataId) {
        return modelResponseBuildService.getModelResponseDTO(summaryDataId);
    }

}