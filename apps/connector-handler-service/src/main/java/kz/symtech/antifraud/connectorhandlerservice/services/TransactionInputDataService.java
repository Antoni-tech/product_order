package kz.symtech.antifraud.connectorhandlerservice.services;

import kz.symtech.antifraud.connectorhandlerservice.dto.TransactionInputDataDTO;
import kz.symtech.antifraud.connectorhandlerservice.dto.TransactionInputDataDTORule;

public interface TransactionInputDataService {
    void save(TransactionInputDataDTO transactionInputDataDTO);
    void save(TransactionInputDataDTORule transactionInputDataDTORule);
    TransactionInputDataDTORule get(Long sDVModelId, Long sDVConnectorId, Long sDVRuleId);
}
