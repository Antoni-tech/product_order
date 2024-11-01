package kz.symtech.antifraud.coreservice.dto;

import java.util.Map;

public record ElasticConnectorSearchDto(Long userId, Map<String, Object> data) {}