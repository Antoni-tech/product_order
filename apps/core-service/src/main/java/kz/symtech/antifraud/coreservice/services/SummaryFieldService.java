package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;

import java.util.List;
import java.util.Map;

public interface SummaryFieldService {
    SummaryField getEntity(Long id);
    List<SummaryField> createFields(SummaryDataVersion summaryDataVersion, Map<String, Object> map);
    void deleteSummaryFields(List<SummaryField> deleteFields);
    void checkForFieldRelation(Long versionId, List<SummaryField> deleteFields);
    List<SummaryField> duplicateFields(SummaryDataVersion summaryDataVersion, SummaryDataVersion duplicateSummaryDataVersion);
}
