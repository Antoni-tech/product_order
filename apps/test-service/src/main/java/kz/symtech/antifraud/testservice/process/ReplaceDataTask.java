package kz.symtech.antifraud.testservice.process;

import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import kz.symtech.antifraud.testservice.services.TestService;
import kz.symtech.antifraud.testservice.services.TransactionDataService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class ReplaceDataTask extends Task {

    public ReplaceDataTask(Long transactionDataId, ScheduledExecutorService executorService, TestService testService, TransactionDataService transactionDataService) {
        super(transactionDataId, executorService, testService, transactionDataService);
    }

    @Override
    public void run() {
        TransactionData transactionData = transactionDataService.get(transactionDataId);
        State state = transactionData.getState();

        if (state.equals(State.COMPLETED) || checkExpiration(transactionData.getExpiresAt())) {
            transactionDataService.updateState(transactionDataId, State.COMPLETED); // if expires
            executorService.shutdown();
        } else if (state.equals(State.IN_PROGRESS)) {
            log.info("ReplacedData. Time: {}. Thread: {}. Id: {}",
                    LocalTime.now(), Thread.currentThread().getName(), transactionData.getId());
            executorService.execute(() -> testService.sendReplace(transactionData));
        }
    }
}
