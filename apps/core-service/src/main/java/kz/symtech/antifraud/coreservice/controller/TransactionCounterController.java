package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.services.SagaTransactionService;
import kz.symtech.antifraud.coreservice.services.TransactionCounterService;
import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction-counter")
@RequiredArgsConstructor
public class TransactionCounterController {

    private final TransactionCounterService transactionCounterService;
    private final SagaTransactionService sagaTransactionService;

    @GetMapping("/{summaryDataVersionIdModel}/{summaryDataVersionId}")
    public TransactionCounterDTO get(
            @PathVariable Long summaryDataVersionIdModel,
            @PathVariable Long summaryDataVersionId) {
        return transactionCounterService.getBySDVModelIdAndSDVId(summaryDataVersionIdModel, summaryDataVersionId);
    }

    @PostMapping("/{summaryDataVersionIdModel}/{summaryDataVersionId}")
    public void reset(
            @PathVariable Long summaryDataVersionIdModel,
            @PathVariable Long summaryDataVersionId) {
        transactionCounterService.resetCounter(summaryDataVersionIdModel, summaryDataVersionId);
    }

    @PostMapping("/{summaryDataVersionIdModel}/{summaryDataVersionId}/set-state")
    public Long setState(
            @PathVariable Long summaryDataVersionIdModel,
            @PathVariable Long summaryDataVersionId,
            @RequestParam TransactionCounterState state) {
        return transactionCounterService.setState(summaryDataVersionIdModel, summaryDataVersionId, state);
    }

    @GetMapping("/{modelId}")
    public List<TransactionCounterDTO> getAllByModelIdAndVersionsId(@PathVariable Long modelId,
                                                                    @RequestParam List<Long> versionsId) {
        return transactionCounterService.getAllByModelIdAndVersionsId(modelId, versionsId);
    }

    @PostMapping("/{summaryDataVersionIdModel}/set-state-all")
    public void setStateToAllComponents(@PathVariable Long summaryDataVersionIdModel,
                                        @RequestParam TransactionCounterState state) {
        sagaTransactionService.updateStateInBothServices(summaryDataVersionIdModel, state);
    }

    @PostMapping("/save-or-update")
    public void saveOrUpdate(@RequestBody TransactionCounterDTO transactionCounterDTO) {
        transactionCounterService.saveOrUpdateTransactionCounter(transactionCounterDTO);
    }

    @PostMapping("/amount/errors/increment/{sDVModelId}/{sDVComponentId}")
    public void incrementAmountErrors(@PathVariable Long sDVModelId,
                                      @PathVariable Long sDVComponentId,
                                      @RequestParam int delta) {
        transactionCounterService.incrementAmountErrors(sDVModelId, sDVComponentId, delta);
    }

    @PostMapping("/amount/transactions/increment/{sDVModelId}/{sDVComponentId}")
    public void incrementAmountTransactions(@PathVariable Long sDVModelId,
                                            @PathVariable Long sDVComponentId) {
        transactionCounterService.incrementAmountTransactions(sDVModelId, sDVComponentId);
    }

    @PostMapping("/update-error/{sDVModelId}/{sDVComponentId}")
    public void updateError(@PathVariable Long sDVModelId, @PathVariable Long sDVComponentId, @RequestBody String error) {
        transactionCounterService.updateLastError(sDVModelId, sDVComponentId, error);
    }
}
