package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.DataStructureDTO;
import kz.symtech.antifraud.coreservice.dto.SummaryDataFilterRequestDTO;
import kz.symtech.antifraud.coreservice.dto.SummaryDataListResponseDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryDataResponseDTO;
import kz.symtech.antifraud.coreservice.services.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data-structure")
public class DataStructureController {
    private final ModelService modelService;

    @PostMapping("/create-or-update")
    public ResponseEntity<Long> createDataStructure(@RequestBody Object object) {
        return ResponseEntity.ok(modelService.createDataStructure(object));
    }

    @PostMapping("/all")
    public ResponseEntity<SummaryDataListResponseDTO> getDataStructureFilter(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam Long userId,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestBody SummaryDataFilterRequestDTO summaryDataFilterRequestDTO) {
        return ResponseEntity.ok(modelService.filter(summaryDataFilterRequestDTO, page, size, userId, orderBy, null));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<DataStructureDTO>> getAll() {
        return ResponseEntity.ok(modelService.getDataStructures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SummaryDataResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.getDataStructure(id));
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Long> duplicate(@RequestParam Long id) {
        return ResponseEntity.ok(modelService.duplicateDataStructure(id));
    }
}
