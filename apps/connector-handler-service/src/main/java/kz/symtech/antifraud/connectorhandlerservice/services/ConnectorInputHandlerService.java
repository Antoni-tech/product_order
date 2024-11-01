package kz.symtech.antifraud.connectorhandlerservice.services;

import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import kz.symtech.antifraud.models.enums.TransactionCounterState;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ConnectorInputHandlerService {
    Set<String> handleConnectorInput(
            UUID summaryDataIdModel, UUID summaryDataIdConnector, Map<String, Object> transaction, Long userId);

    void changeState(UUID summaryDataIdModel, TransactionCounterState transactionCounterState);

    void changeModel(UUID summaryDataIdModel, ModelStructResponseDTO modelStructResponseDTO);
}
