package kz.symtech.antifraud.coreservice.services;

import java.util.Map;

public interface CountDocumentService {
    Map<String, Long> countDocumentsById(Long userId, String fieldName);
    Long countDocumentsByRuleId(Long userId, Long ruleId);
}
