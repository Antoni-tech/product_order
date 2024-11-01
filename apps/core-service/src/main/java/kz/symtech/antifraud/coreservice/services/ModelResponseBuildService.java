package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;

import java.util.UUID;

public interface ModelResponseBuildService {
    ModelStructResponseDTO getModelResponseDTO(UUID summaryDataId);
}
