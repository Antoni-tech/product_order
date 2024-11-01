package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorInput;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorOutput;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.repository.ModelConnectorInputRepository;
import kz.symtech.antifraud.coreservice.repository.ModelConnectorOutputRepository;
import kz.symtech.antifraud.coreservice.services.ConnectorCronService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class ConnectorCronServiceImpl implements ConnectorCronService {

    private final TaskScheduler taskScheduler;
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledTasks;
    private final ModelConnectorInputRepository modelConnectorInputRepository;
    private final ModelConnectorOutputRepository modelConnectorOutputRepository;

    public ConnectorCronServiceImpl(TaskScheduler taskScheduler,
                                    ModelConnectorInputRepository modelConnectorInputRepository,
                                    ModelConnectorOutputRepository modelConnectorOutputRepository) {
        this.taskScheduler = taskScheduler;
        this.scheduledTasks = new ConcurrentHashMap<>();
        this.modelConnectorInputRepository = modelConnectorInputRepository;
        this.modelConnectorOutputRepository = modelConnectorOutputRepository;
    }

    @Override
    public void start(Long sDVConnectorId, SummaryDataType type) {
        if (Objects.nonNull(scheduledTasks.get(sDVConnectorId))) {
            log.info("Connector {} already in process", sDVConnectorId);
            return;
        }

        Pair<String, String> connectorInfo = getConnectorInfo(sDVConnectorId, type);
        if (Objects.nonNull(connectorInfo) && !connectorInfo.getFirst().isEmpty() && !connectorInfo.getSecond().isEmpty()) {
            log.info("Starting task for connector {} with cron {}", sDVConnectorId, connectorInfo.getSecond());
            ScheduledFuture<?> scheduledFuture =
                    taskScheduler.schedule(() -> getInfo(sDVConnectorId, connectorInfo.getFirst()), new CronTrigger(connectorInfo.getSecond()));
            if (Objects.nonNull(scheduledFuture)) {
                scheduledTasks.put(sDVConnectorId, scheduledFuture);
                log.info("Time: {}, task for connector {} started successfully", LocalDateTime.now(), sDVConnectorId);
            } else {
                log.error("Failed to start task for connector {}", sDVConnectorId);
            }
        } else {
            log.error("Failed to start task for component {}", sDVConnectorId);
        }
    }

    private Pair<String, String> getConnectorInfo(Long sDVConnectorId, SummaryDataType type) {
        if (type.equals(SummaryDataType.CONNECTOR_INPUT)) {
            Optional<ModelConnectorInput> modelConnectorInput = modelConnectorInputRepository.findBySummaryDataVersionId(sDVConnectorId);
            if (modelConnectorInput.isPresent()) {
                return Pair.of(modelConnectorInput.get().getUrlForInfo(), modelConnectorInput.get().getCron());
            }
        } else if (type.equals(SummaryDataType.CONNECTOR_OUTPUT)) {
            Optional<ModelConnectorOutput> modelConnectorOutput = modelConnectorOutputRepository.findBySummaryDataVersionId(sDVConnectorId);
            if (modelConnectorOutput.isPresent()) {
                return Pair.of(modelConnectorOutput.get().getUrlForInfo(), modelConnectorOutput.get().getCron());
            }
        }

        return null;
    }

    @Override
    public void stop(Long sDVConnectorId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(sDVConnectorId);
        if (scheduledFuture != null) {
            log.info("Time {}, stopping task for connector {}", LocalDateTime.now(), sDVConnectorId);
            scheduledFuture.cancel(true);
            scheduledTasks.remove(sDVConnectorId);
        }
    }

    public void getInfo(Long sDVConnectorId, String url) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Object> res = restTemplate.getForEntity(url, Object.class);
            log.info("Connector {}, time {}, data {}", sDVConnectorId, LocalDateTime.now(), res.getBody());
        } catch (Exception e) {
            log.info("Something went wrong due to {}", e.getMessage());
        }
    }
}
