package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.models.enums.TransactionCounterState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModelStructFilterDTO extends SummaryDataResponseDTO {
    private TransactionCounterState state;
    private int amountOfTransactions;
    private int amountOfErrors;
    private List<ModelStructComponentsDTO> modelStructComponents;
}