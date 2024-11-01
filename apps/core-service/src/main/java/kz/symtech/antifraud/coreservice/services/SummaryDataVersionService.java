package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;

import java.util.List;
import java.util.UUID;

public interface SummaryDataVersionService {
    SummaryDataVersion getBySummaryDataIdAndActive(UUID summaryDataId);
    List<SummaryDataVersion> getAll();
    SummaryDataVersion get(Long id);
}
