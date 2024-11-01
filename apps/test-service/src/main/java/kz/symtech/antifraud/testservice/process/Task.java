package kz.symtech.antifraud.testservice.process;

import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import kz.symtech.antifraud.testservice.services.TestService;
import kz.symtech.antifraud.testservice.services.TransactionDataService;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class Task implements Runnable {

    protected final Long transactionDataId;
    protected final ScheduledExecutorService executorService;
    protected final TestService testService;
    protected final TransactionDataService transactionDataService;

    public Task(
            Long transactionDataId,
            ScheduledExecutorService executorService,
            TestService testService,
            TransactionDataService transactionDataService) {
        this.transactionDataId = transactionDataId;
        this.executorService = executorService;
        this.testService = testService;
        this.transactionDataService = transactionDataService;
    }

    @Override
    public void run() {
        TransactionData transactionData = transactionDataService.get(transactionDataId);
        State state = transactionData.getState();

        if (state.equals(State.COMPLETED) || checkExpiration(transactionData.getExpiresAt())) {
            transactionDataService.updateState(transactionDataId, State.COMPLETED); // if expires
            executorService.shutdown();
        } else if (state.equals(State.IN_PROGRESS)) {
            log.info("Time: {}. Thread: {}. Id: {}",
                    LocalTime.now(), Thread.currentThread().getName(), transactionData.getId());
            executorService.execute(() -> testService.send(transactionData));
        }
    }

    public Boolean checkExpiration(LocalDateTime expiresAt) {
        ZoneId zoneId = ZoneOffset.UTC;

        ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(zoneId);
        ZonedDateTime expires = expiresAt.atZone(zoneId);

        return zonedDateTimeNow.isAfter(expires);
    }
}
