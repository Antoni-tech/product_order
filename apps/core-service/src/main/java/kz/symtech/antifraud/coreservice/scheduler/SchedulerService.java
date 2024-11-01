package kz.symtech.antifraud.coreservice.scheduler;

import kz.symtech.antifraud.coreservice.services.ElasticsearchService;
import kz.symtech.antifraud.coreservice.services.TransactionCounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class SchedulerService {

    private final ElasticsearchService elasticsearchService;
    private final TransactionCounterService transactionCounterService;

    // This method will run once a day at 14:30
    @Scheduled(cron = "0 30 14 * * ?")
    public void clearConnectors() {
        log.info("updating data of connector conflicts before clearing");
        elasticsearchService.deleteExpiredDocuments();
    }

    @Scheduled(fixedRateString = "${scheduler.save-transactions}")
    public void addTransactionsCounter() {
        transactionCounterService.save();
    }

}
