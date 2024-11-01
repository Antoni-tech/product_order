package kz.symtech.antifraud.feignclients.clients.services;

import kz.symtech.antifraud.models.dto.ElasticSaveRequestDTO;
import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "core-service")
public interface CoreServiceClient {
    @PostMapping("core-service/api/v1/elastic/add-transaction/{summaryDataIdModel}/{summaryDataIdConnector}/{userId}")
    Set<String> addTransaction(@PathVariable UUID summaryDataIdModel,
                               @PathVariable UUID summaryDataIdConnector,
                               @PathVariable Long userId,
                               @RequestBody Map<String, Object> data);

    @PostMapping("core-service/api/v1/elastic/save-document")
    String saveDocument(@RequestBody ElasticSaveRequestDTO elasticSaveRequestDTO);

    @PostMapping("core-service/api/v1/transaction-counter/{summaryDataVersionIdModel}/{summaryDataVersionId}")
    void resetCounter(@PathVariable Long summaryDataVersionIdModel,
                      @PathVariable Long summaryDataVersionId);

    @GetMapping("core-service/api/model/info/{summaryDataId}")
    ModelStructResponseDTO getModelInfo(@PathVariable UUID summaryDataId);

    @GetMapping("core-service/api/connector/getUrl/{id}")
    String getUrl(@PathVariable Long id);

    @PostMapping("core-service/api/v1/transaction-counter/save-or-update")
    void saveOrUpdateTransactionCounter(@RequestBody TransactionCounterDTO transactionCounterDTO);

    @GetMapping("core-service/api/v1/transaction-counter/{summaryDataVersionIdModel}/{summaryDataVersionId}")
    TransactionCounterDTO getTransactionCounter(@PathVariable Long summaryDataVersionIdModel,
                                                @PathVariable Long summaryDataVersionId);

    @PostMapping("core-service/api/v1/transaction-counter/amount/errors/increment/{sDVModelId}/{sDVComponentId}")
    void incrementErrors(@PathVariable Long sDVModelId, @PathVariable Long sDVComponentId, @RequestParam int delta);

    @PostMapping("core-service/api/v1/transaction-counter/amount/transactions/increment/{sDVModelId}/{sDVComponentId}")
    void incrementTransactions(@PathVariable Long sDVModelId, @PathVariable Long sDVComponentId);

    @PostMapping("core-service/api/v1/transaction-counter/update-error/{sDVModelId}/{sDVComponentId}")
    void updateError(@PathVariable Long sDVModelId, @PathVariable Long sDVComponentId, @RequestBody String error);
}
