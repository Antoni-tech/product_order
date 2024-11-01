package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationDeleteRequestDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationRequestDTO;
import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationResponseDTO;
import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;

import java.util.List;

public interface FieldRelationService {
    Long saveOrUpdate(FieldRelationRequestDTO fieldRelationRequestDTO);
    List<FieldRelationDTO> getFieldRelations(Long summaryDataVersionModelId);
    List<FieldRelationResponseDTO> getAllBySDVModelId(Long summaryDataVersionModelId);
    void delete(FieldRelationDeleteRequestDTO fieldRelationDeleteRequestDTO);
}
