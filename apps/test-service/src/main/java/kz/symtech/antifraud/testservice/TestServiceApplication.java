package kz.symtech.antifraud.testservice;

import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import kz.symtech.antifraud.testservice.services.TestService;
import kz.symtech.antifraud.testservice.services.TransactionDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Objects;

@SpringBootApplication
@EnableFeignClients(basePackages = "kz.symtech.antifraud.feignclients.clients.services")
public class TestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean(TestService testService, TransactionDataService transactionDataService) {
        return (args) -> {
            List<TransactionData> transactionDataListFiltered = transactionDataService.getAll()
                    .stream()
                    .filter(transactionData -> transactionData.getState().equals(State.IN_PROGRESS))
                    .toList();

            transactionDataListFiltered.forEach(transactionData -> {
                if (transactionData.getIsCyclical()) {
                    testService.execute(transactionData);
                    if (
                            Objects.nonNull(transactionData.getDataToReplace())
                                    && transactionData.getDelayToReplace() != 0
                                    && Objects.nonNull(transactionData.getChronoUnitToReplace())) {
                        testService.executeReplace(transactionData);
                    }
                } else {
                    testService.send(transactionData);
                }
            });
        };
    }
}