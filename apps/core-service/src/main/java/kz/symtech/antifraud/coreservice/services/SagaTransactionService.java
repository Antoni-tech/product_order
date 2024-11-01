package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.models.enums.TransactionCounterState;

public interface SagaTransactionService {
    void updateStateInBothServices(Long summaryDataVersionIdModel, TransactionCounterState state);
}
