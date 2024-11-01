package kz.symtech.antifraud.testservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.symtech.antifraud.feignclients.clients.services.CoreServiceClient;
import kz.symtech.antifraud.testservice.dto.TransactionDataRequestDto;
import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import kz.symtech.antifraud.testservice.exceptions.AlreadyRunningException;
import kz.symtech.antifraud.testservice.process.ReplaceDataTask;
import kz.symtech.antifraud.testservice.process.Task;
import kz.symtech.antifraud.testservice.services.CommonService;
import kz.symtech.antifraud.testservice.services.TestService;
import kz.symtech.antifraud.testservice.services.TransactionDataService;
import kz.symtech.antifraud.testservice.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class TestServiceImpl implements TestService, CommonService {

    private static final String REPLACE_DATA_PREFIX = "replace";

    private final CoreServiceClient coreServiceClient;
    private final ObjectMapper objectMapper;
    private final TransactionDataService transactionDataService;
    private final CommonService commonService;
    private final RedisUtil redisUtil;
    private static final Object lock = new Object();
    private final Map<String, ScheduledExecutorService> executorServiceMap = new ConcurrentHashMap<>();

    @Value("${redis-prefixes.counter-requests-test}")
    private String counterRequestsTestRedisPrefix;

    @Override
    public void startStopTransaction(Long id) {
        TransactionData transactionData = transactionDataService.get(id);
        if (transactionData.getState().equals(State.IN_PROGRESS)) {
            stopMainAndSubTransaction(id.toString());
            transactionDataService.updateState(id, State.COMPLETED);
        } else {
            if (transactionData.getIsCyclical()) {
                execute(transactionData);
                if (
                        Objects.nonNull(transactionData.getDataToReplace())
                                && transactionData.getDelayToReplace() != 0
                                && Objects.nonNull(transactionData.getChronoUnitToReplace())) {
                    executeReplace(transactionData);
                }
            } else {
                send(transactionData);
            }
        }
    }

    private void stopMainAndSubTransaction(String id) {
        stopTransaction(id);
        stopTransaction(REPLACE_DATA_PREFIX + id);
    }

    @Override
    public Long saveTransactionData(TransactionDataRequestDto transactionDataRequestDto) {
        if (Objects.nonNull(transactionDataRequestDto.getId())) {
            stopMainAndSubTransaction(transactionDataRequestDto.getId().toString());
        }
        return commonService.saveTransactionData(transactionDataRequestDto);
    }

    public void stopTransaction(String id) {
        ScheduledExecutorService executorService = executorServiceMap.get(id);
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            executorServiceMap.remove(id);
        }
    }

    @Override
    public void execute(TransactionData transactionData) {
        checkIfTransactionDataWithSameSummaryDataModelIdAndSummaryConnectorIdRunning(transactionData);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorServiceMap.put(transactionData.getId().toString(), executorService);

        Task task = new Task(transactionData.getId(), executorService, this, transactionDataService);
        transactionDataService.updateState(transactionData.getId(), State.IN_PROGRESS);
        executeTask(task, executorService, transactionData.getDelay(), transactionData.getChronoUnit());
    }

    @Override
    public void executeReplace(TransactionData transactionData) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorServiceMap.put(REPLACE_DATA_PREFIX + transactionData.getId(), executorService);

        Task taskToReplace = new ReplaceDataTask(transactionData.getId(), executorService, this, transactionDataService);
        executeTask(taskToReplace, executorService, transactionData.getDelayToReplace(), transactionData.getChronoUnitToReplace());
    }

    private void executeTask(Task task, ScheduledExecutorService scheduledExecutorService, int delay, ChronoUnit unit) {
        scheduledExecutorService.scheduleWithFixedDelay(task, 0, delay, TimeUnit.of(unit));
    }

    private void checkIfTransactionDataWithSameSummaryDataModelIdAndSummaryConnectorIdRunning(TransactionData transactionData) {
        TransactionData transactionDataFromDB = transactionDataService
                .getBySummaryDataModelIdAndSummaryDataConnectorId(
                        transactionData.getSummaryDataModelId(),
                        transactionData.getSummaryDataConnectorId());

        if (transactionDataFromDB.getState().equals(State.IN_PROGRESS)) {
            throw new AlreadyRunningException(String.format(
                    "TransactionData with summaryDataModelId %s and summaryDataConnectorId %s already in progress",
                    transactionData.getSummaryDataModelId(), transactionData.getSummaryDataConnectorId()));
        }
    }

    @Override
    public void send(TransactionData transactionData) {
        Map<String, Object> map = getMap(transactionData.getData());

        IntStream
                .range(0, transactionData.getNumberOfRepetitions())
                .forEach(t -> coreServiceClient.addTransaction(
                        transactionData.getSummaryDataModelId(),
                        transactionData.getSummaryDataConnectorId(),
                        transactionData.getUserId(),
                        map));

        countRequests(transactionData.getNumberOfRepetitions());
    }

    @Override
    public void sendReplace(TransactionData transactionData) {
        Map<String, Object> map = getMap(transactionData.getDataToReplace());

        coreServiceClient.addTransaction(
                transactionData.getSummaryDataModelId(),
                transactionData.getSummaryDataConnectorId(),
                transactionData.getUserId(),
                map);

        countRequests(1);
    }

    private Map<String, Object> getMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void countRequests(int numberOfRepetitions) {
        synchronized (lock) {
            redisUtil.increment(counterRequestsTestRedisPrefix, numberOfRepetitions);
        }
    }
}
