package kz.symtech.antifraud.coreservice.services.impl;

import feign.FeignException;
import kz.symtech.antifraud.coreservice.exceptions.ApplicationException;
import kz.symtech.antifraud.coreservice.services.ModelResponseBuildService;
import kz.symtech.antifraud.coreservice.services.SagaTransactionService;
import kz.symtech.antifraud.coreservice.services.TransactionCounterService;
import kz.symtech.antifraud.feignclients.clients.services.HandlerService;
import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaTransactionServiceImpl implements SagaTransactionService {

    private final TransactionCounterService transactionCounterService;
    private final HandlerService handlerService;
    private final ModelResponseBuildService modelResponseBuildService;

    @Override
    public void updateStateInBothServices(Long summaryDataVersionIdModel, TransactionCounterState state) {
        try {
            UUID summaryDataIdModel = transactionCounterService.setStateToAll(summaryDataVersionIdModel, state);
            ModelStructResponseDTO modelStructResponseDTO = modelResponseBuildService.getModelResponseDTO(summaryDataIdModel);
            switch (state) {
                case RUN -> handlerService.changeModel(summaryDataIdModel, modelStructResponseDTO);
                case STOP, PAUSE -> handlerService.changeState(summaryDataIdModel, state);
            }
        } catch (FeignException e) {
            throw new ApplicationException("Something went wrong during update state", HttpStatus.valueOf(e.status()));
        }
    }
}
