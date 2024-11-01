package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.dto.ElasticConnectorSearchDto;
import kz.symtech.antifraud.coreservice.enums.ConnectorConflictType;
import kz.symtech.antifraud.models.dto.ElasticSaveRequestDTO;

import java.util.List;
import java.util.Map;

public interface ElasticsearchService {
    void createIndex(Long userId);
    List<Object> searchByFields(ElasticConnectorSearchDto elasticConnectorSearchDto);
    void deleteExpiredDocuments();
    Map<ConnectorConflictType, Map<String, Long>> countErrorsAndRules();
    String saveDocument(ElasticSaveRequestDTO elasticSaveRequestDTO);
}
