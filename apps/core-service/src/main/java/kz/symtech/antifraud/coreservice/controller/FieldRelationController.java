package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationDeleteRequestDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationRequestDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationResponseDTO;
import kz.symtech.antifraud.coreservice.services.FieldRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/field-relations")
@RequiredArgsConstructor
public class FieldRelationController {

    private final FieldRelationService fieldRelationService;
    @PostMapping
    public Long save(@RequestBody FieldRelationRequestDTO fieldRelationRequestDTO) {
        return fieldRelationService.saveOrUpdate(fieldRelationRequestDTO);
    }

    @GetMapping("/{sDVModelId}")
    public List<FieldRelationResponseDTO> getBySDVModelId(@PathVariable Long sDVModelId) {
        return fieldRelationService.getAllBySDVModelId(sDVModelId);
    }

    @DeleteMapping
    public void delete(@RequestBody FieldRelationDeleteRequestDTO fieldRelationDeleteRequestDTO) {
        fieldRelationService.delete(fieldRelationDeleteRequestDTO);
    }
}
