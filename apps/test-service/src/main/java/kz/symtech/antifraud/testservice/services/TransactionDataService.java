package kz.symtech.antifraud.testservice.services;

import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;

import java.util.List;
import java.util.UUID;

public interface TransactionDataService {
    TransactionData get(Long id);
    List<TransactionData> getAll();
    List<TransactionData> getAllByState(State state);
    void delete(Long id);
    TransactionData getBySummaryDataModelIdAndSummaryDataConnectorId(UUID summaryDataModelId, UUID summaryDataConnectorId);
    void updateState(Long transactionDataId, State state);
}
