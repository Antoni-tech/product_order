package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import kz.symtech.antifraud.models.enums.TransactionCounterState;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionCounterService {
    void save();
    TransactionCounterDTO getBySDVModelIdAndSDVId(Long summaryDataVersionIdModel, Long summaryDataVersionId);
    void resetCounter(Long summaryDataVersionIdModel, Long summaryDataVersionId);
    Long setState(Long summaryDataVersionIdModel, Long summaryDataVersionId, TransactionCounterState state);
    List<TransactionCounterDTO> getAllByModelIdAndVersionsId(Long summaryDataVersionIdModel, List<Long> versionsId);
    void saveOrUpdateTransactionCounter(TransactionCounterDTO transactionCounterDTO);
    Map<Long, TransactionCounterDTO> getAllBySDVModelId(Long summaryDataVersionIdModel);
    UUID setStateToAll(Long summaryDataVersionIdModel, TransactionCounterState state);
    int getAmount(Long sDVModelId, Long sDVComponentId, Boolean isAmountOfErrors);
    void incrementAmountErrors(Long sDVModelId, Long sDVComponentId, int delta);
    void incrementAmountTransactions(Long sDVModelId, Long sDVComponentId);
    void updateLastError(Long sDVModelId, Long sDVComponentId, String error);
}
