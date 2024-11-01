package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.models.TransactionCounter;
import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionCounterMapper implements Function<TransactionCounter, TransactionCounterDTO> {
    @Override
    public TransactionCounterDTO apply(TransactionCounter transactionCounter) {
        return TransactionCounterDTO.builder()
                .id(transactionCounter.getId())
                .state(transactionCounter.getState().name())
                .summaryDataVersionModelId(transactionCounter.getModelStruct().getSummaryDataVersion().getId())
                .summaryDataVersionId(transactionCounter.getSummaryDataVersion().getId())
                .build();
    }
}
