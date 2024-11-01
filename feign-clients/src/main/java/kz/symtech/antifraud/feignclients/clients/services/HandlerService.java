package kz.symtech.antifraud.feignclients.clients.services;

import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "connector-handler-service")
public interface HandlerService {

    @PostMapping("connector-handler-service/api/v1/connector-handler/state/{summaryDataIdModel}")
    void changeState(@PathVariable UUID summaryDataIdModel, @RequestParam TransactionCounterState state);

    @PostMapping("connector-handler-service/api/v1/connector-handler/model/{summaryDataIdModel}")
    void changeModel(@PathVariable UUID summaryDataIdModel, @RequestBody ModelStructResponseDTO modelStructResponseDTO);
}
