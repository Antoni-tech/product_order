package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.dto.*;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryDataResponseDTO;
import kz.symtech.antifraud.coreservice.dto.ModelConnectorDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelRuleGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.get.response.ModelStructGetResponseDTO;
import kz.symtech.antifraud.coreservice.dto.model.struct.request.ModelStructRequestDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorInput;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.ModelRuleCacheDTO;

import java.util.List;
import java.util.Map;

public interface ModelService {
    Long createConnector(Object object);

    Long createModelRule(ModelRuleCreateRequestDTO modelRuleCreateRequestDTO);

    Long createModelStruct(ModelStructRequestDTO modelStructRequestDTO);

    SummaryVersionState publishOrCancel(Long id, SummaryVersionState state);

    void deleteDraftOrCanceled(Long id);

    Boolean changeActiveVersion(Long id);

    SummaryDataListResponseDTO filter(SummaryDataFilterRequestDTO summaryDataFilterRequestDTO, Long page, Long size, Long userId, String orderBy, List<SummaryDataType> types);

    List<ModelConnectorDTO> getAllConnectors();

    List<ModelRuleDTO> getModelRules();

    List<ModelStructGetResponseDTO> getModels();

    Object getConnector(Long id);

    ModelRuleGetResponseDTO getModelRule(Long id, Long modelId);

    SummaryDataResponseDTO getModel(Long id);

    Long duplicateConnector(Long id);

    Long duplicateRule(Long id);

    Long duplicateModel(Long id);

    List<SummaryDataResponseDTO> historyOfChanges(Long id);

    ModelConnectorInput getModelConnectorInputEntity(Long summaryDataVersionId);

    Map<Long, ModelRuleCacheDTO> getModelRulesCacheDTOByIds(Map<Long, ComponentModel> modelRuleComponents);

    Object getAccessFields(Long sDVModelId, Long sdvRuleId);

    Long createDataStructure(Object object);

    List<DataStructureDTO> getDataStructures();

    SummaryDataResponseDTO getDataStructure(Long id);

    Long duplicateDataStructure(Long id);

    ModelConnectorOutputDTO getModelConnectorOutputByVersionId(Long sDVId);

    List<ComponentModel> getModelComponents(Long summaryDataVersionId);

    String getUrlConnectorOutput(Long id);


}