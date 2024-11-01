package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.coreservice.entities.models.TransactionCounter;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.exceptions.RelationEstablishException;
import kz.symtech.antifraud.coreservice.mapper.TransactionCounterMapper;
import kz.symtech.antifraud.coreservice.repository.ModelStructComponentsRepository;
import kz.symtech.antifraud.coreservice.repository.TransactionCounterRepository;
import kz.symtech.antifraud.coreservice.services.ConnectorCronService;
import kz.symtech.antifraud.coreservice.services.FieldRelationService;
import kz.symtech.antifraud.coreservice.services.TransactionCounterService;
import kz.symtech.antifraud.coreservice.utils.RedisUtil;
import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionCounterServiceImpl implements TransactionCounterService {

    private final TransactionCounterRepository transactionCounterRepository;
    private final RedisUtil redisUtil;
    private final ModelStructComponentsRepository modelStructComponentsRepository;
    private final TransactionCounterMapper transactionCounterMapper;
    private final FieldRelationService fieldRelationService;
    private final ConnectorCronService connectorCronService;

    @Value("${redis-prefixes.transaction}")
    private String transactionCounterRedisPrefix;

    @Value("${redis-prefixes.amount-of-transactions}")
    private String amountOfTransactionRedisPrefix;

    @Value("${redis-prefixes.amount-of-transaction-errors}")
    private String amountOfTransactionErrorsRedisPrefix;

    @Override
    public void save() {
        List<ModelStructComponents> modelStructComponents = modelStructComponentsRepository.findAll();

        List<TransactionCounter> transactionCounters = modelStructComponents.stream()
                .map(msc -> {
                    Long summaryDataVersionIdModel = msc.getModelStruct().getSummaryDataVersion().getId();
                    Long summaryDataVersionIdComponent = msc.getSummaryDataVersion().getId();

                    String hashKey = amountOfTransactionRedisPrefix + ":" + summaryDataVersionIdModel;
                    String hashKeyErrors = amountOfTransactionErrorsRedisPrefix + ":" + summaryDataVersionIdModel;
                    String key = summaryDataVersionIdComponent.toString();

                    Object amountOfTransactions = redisUtil.getValue(hashKey, key);
                    Object amountOfTransactionErrors = redisUtil.getValue(hashKeyErrors, key);

                    TransactionCounter transactionCounter = getEntityBySummaryDataVersionModelIdAndSummaryDataVersion(
                            summaryDataVersionIdModel, summaryDataVersionIdComponent);

                    if (Objects.nonNull(amountOfTransactions)) {
                        transactionCounter.setAmountOfTransactions((int) amountOfTransactions);
                    }
                    if (Objects.nonNull(amountOfTransactionErrors)) {
                        transactionCounter.setAmountOfErrors((int) amountOfTransactionErrors);
                    }
                    if (transactionCounter.getState() == null) {
                        transactionCounter.setState(TransactionCounterState.STOP);
                    }
                    transactionCounter.setSummaryDataVersion(msc.getSummaryDataVersion());
                    transactionCounter.setModelStruct(msc.getModelStruct());

                    return transactionCounter;
                })
                .collect(Collectors.toList());

        log.info("saving all transaction counters");
        transactionCounterRepository.saveAll(transactionCounters);
    }

    @Override
    public List<TransactionCounterDTO> getAllByModelIdAndVersionsId(Long summaryDataVersionIdModel, List<Long> versionsId) {
        String hashKey = transactionCounterRedisPrefix + ":" + summaryDataVersionIdModel;

        List<String> keys = versionsId.stream().map(Object::toString).toList();
        List<Object> objects = redisUtil.getAllValueByHashKeyAndKeys(hashKey, keys);

        if(Objects.nonNull(objects) && !objects.isEmpty()) {
            return objects.stream()
                    .map(t -> (TransactionCounterDTO) t)
                    .filter(t -> versionsId.contains(t.getSummaryDataVersionId()))
                    .toList();
        }

        List<TransactionCounterDTO> transactionCounterDTOs = transactionCounterRepository
                .findByModelStructSummaryDataVersionIdAndSummaryDataVersionIdIn(summaryDataVersionIdModel, versionsId)
                .stream()
                .map(transactionCounterMapper)
                .toList();

        log.info("Saving transactionCounterDTOs");
        putListToRedis(summaryDataVersionIdModel, transactionCounterDTOs);

        return transactionCounterDTOs;
    }

    private TransactionCounter getEntityBySummaryDataVersionModelIdAndSummaryDataVersion(
            Long summaryDataVersionModelId, Long summaryDataVersionId) {
        TransactionCounter transactionCounter = transactionCounterRepository
                .findByModelStructSummaryDataVersionIdAndSummaryDataVersionId(summaryDataVersionModelId, summaryDataVersionId);

        if (Objects.isNull(transactionCounter)) {
            transactionCounter = new TransactionCounter();
        }

        return transactionCounter;
    }

    @Override
    public Map<Long, TransactionCounterDTO> getAllBySDVModelId(Long summaryDataVersionIdModel) {
        String hashKey = transactionCounterRedisPrefix + ":" + summaryDataVersionIdModel;

        List<Object> objects = redisUtil.getAllValueByHashKey(hashKey);

        if(Objects.nonNull(objects) && !objects.isEmpty()) {
            return objects.stream()
                    .map(t -> (TransactionCounterDTO) t)
                    .collect(Collectors.toMap(TransactionCounterDTO::getSummaryDataVersionId, Function.identity()));
        }

        Map<Long, TransactionCounterDTO> transactionCounterDTOMap = transactionCounterRepository
                .findAllByModelStructSummaryDataVersionId(summaryDataVersionIdModel)
                .stream()
                .map(transactionCounterMapper)
                .collect(Collectors.toMap(TransactionCounterDTO::getSummaryDataVersionId, Function.identity()));

        log.info("Saving transactionCounterDTOs");
        putListToRedis(summaryDataVersionIdModel, transactionCounterDTOMap.values().stream().toList());

        return transactionCounterDTOMap;
    }

    @Override
    public TransactionCounterDTO getBySDVModelIdAndSDVId(Long summaryDataVersionIdModel, Long summaryDataVersionIdComponent) {
        TransactionCounter transactionCounter = transactionCounterRepository.findByModelStructSummaryDataVersionIdAndSummaryDataVersionId(
                summaryDataVersionIdModel,
                summaryDataVersionIdComponent);
        return Objects.nonNull(transactionCounter) ? transactionCounterMapper.apply(transactionCounter) : null;
    }

    @Override
    @Transactional
    public void resetCounter(Long summaryDataVersionIdModel, Long summaryDataVersionId) {
        TransactionCounter transactionCounter = transactionCounterRepository
                .findByModelStructSummaryDataVersionIdAndSummaryDataVersionId(summaryDataVersionIdModel, summaryDataVersionId);

        if (Objects.isNull(transactionCounter)) {
            throw new NotFoundException(String.format("TransactionCounter not found '%s'", summaryDataVersionId));
        }

        transactionCounter.setAmountOfErrors(0);
        transactionCounter.setAmountOfTransactions(0);
        transactionCounterRepository.save(transactionCounter);

        deleteCounters(summaryDataVersionIdModel, summaryDataVersionId);
    }

    public void deleteCounters(Long sDVModelId, Long sDVComponentId) {
        String hashKey = amountOfTransactionRedisPrefix + ":" + sDVModelId;
        String hashKeyErrors = amountOfTransactionErrorsRedisPrefix + ":" + sDVModelId;
        String key = sDVComponentId.toString();

        redisUtil.delete(hashKey, key);
        redisUtil.delete(hashKeyErrors, key);
    }

    @Override
    @Transactional
    public Long setState(Long summaryDataVersionIdModel, Long summaryDataVersionIdComponent, TransactionCounterState state) {
        TransactionCounter transactionCounter =
                getEntityBySummaryDataVersionModelIdAndSummaryDataVersion(summaryDataVersionIdModel, summaryDataVersionIdComponent);

        SummaryDataType typeOfComponent = transactionCounter.getSummaryDataVersion().getSummaryData().getType();
        if (typeOfComponent.equals(SummaryDataType.RULE) && state.equals(TransactionCounterState.RUN)) {
            checkIfAllRelationsEstablished(
                    transactionCounter.getSummaryDataVersion().getSummaryFields(),
                    summaryDataVersionIdModel,
                    summaryDataVersionIdComponent
            );
        } else if (state.equals(TransactionCounterState.STOP)) {
            resetCounter(summaryDataVersionIdModel, summaryDataVersionIdComponent);
        }

        transactionCounter.setState(state);

        TransactionCounterDTO transactionCounterDTO =
                getBySDVModelIdAndSDVId(summaryDataVersionIdModel, summaryDataVersionIdComponent);
        transactionCounterDTO.setState(transactionCounter.getState().name());

        String hashKey = transactionCounterRedisPrefix + ":" + summaryDataVersionIdModel;
        String key = summaryDataVersionIdComponent.toString();

        redisUtil.putValue(hashKey, key, transactionCounterDTO);

        return transactionCounterRepository.save(transactionCounter).getId();
    }

    private void checkIfAllRelationsEstablished(List<SummaryField> summaryFields, Long sDVModelId, Long sDVComponentId) {
        List<Long> summaryFieldIdList = summaryFields
                .stream()
                .filter(x -> x.getDefaultField().equals(false))
                .map(BaseEntity::getId)
                .toList();

        List<Long> fieldRelationSummaryRuleIds =
                fieldRelationService.getFieldRelations(sDVModelId)
                        .stream()
                        .filter(x -> x.getSummaryDataVersionId().equals(sDVComponentId))
                        .map(FieldRelationDTO::getSummaryFieldId)
                        .toList();

        List<String> relationErrors = new ArrayList<>();

        summaryFieldIdList.forEach(id -> {
            if (!fieldRelationSummaryRuleIds.contains(id)) {
                relationErrors.add(String.format("There is no relation of summary field with id %s", id));
            }
        });

        if (!relationErrors.isEmpty()) {
            throw new RelationEstablishException(relationErrors);
        }
    }

    @Override
    public void saveOrUpdateTransactionCounter(TransactionCounterDTO transactionCounterDTO) {
        Long sDVModelId = transactionCounterDTO.getSummaryDataVersionModelId();
        Long sDVComponentId = transactionCounterDTO.getSummaryDataVersionId();

        TransactionCounter transactionCounter =
                getEntityBySummaryDataVersionModelIdAndSummaryDataVersion(sDVModelId, sDVComponentId);

        transactionCounter.setAmountOfTransactions(getAmount(sDVModelId, sDVComponentId, false));
        transactionCounter.setAmountOfErrors(getAmount(sDVModelId, sDVComponentId, true));

        transactionCounterRepository.save(transactionCounter);
    }

    @Override
    public int getAmount(Long sDVModelId, Long sDVComponentId, Boolean isAmountOfErrors) {
        String hashKey;
        if (isAmountOfErrors) {
            hashKey = amountOfTransactionErrorsRedisPrefix;
        } else {
            hashKey = amountOfTransactionRedisPrefix;
        }
        hashKey += ":" + sDVModelId;
        String key = sDVComponentId.toString();

        Object value = redisUtil.getValue(hashKey, key);
        if (Objects.isNull(value)) {
            TransactionCounter transactionCounter = transactionCounterRepository
                    .findByModelStructSummaryDataVersionIdAndSummaryDataVersionId(sDVModelId, sDVComponentId);

            return (Objects.nonNull(transactionCounter)) ? (isAmountOfErrors ? transactionCounter.getAmountOfErrors() : transactionCounter.getAmountOfTransactions()) : 0;
        }

        return (int) value;
    }

    @Override
    public UUID setStateToAll(Long summaryDataVersionIdModel, TransactionCounterState state) {
        List<TransactionCounter> transactionCounters =
                transactionCounterRepository.findAllByModelStructSummaryDataVersionId(summaryDataVersionIdModel);

        UUID summaryDataIdModel = !transactionCounters.isEmpty()
                ? transactionCounters.get(0).getModelStruct().getSummaryDataVersion().getSummaryData().getId()
                : null;
        if (Objects.isNull(summaryDataIdModel)) {
            throw new NotFoundException(String.format("Model %s not found", summaryDataVersionIdModel));
        }

        if (state.equals(TransactionCounterState.STOP)) {
            transactionCounters.forEach(transactionCounter -> {
                transactionCounter.setAmountOfTransactions(0);
                transactionCounter.setAmountOfErrors(0);

                deleteCounters(summaryDataVersionIdModel, transactionCounter.getSummaryDataVersion().getId());
            });
        }

        transactionCounters.forEach(transactionCounter -> {
            SummaryDataType typeOfComponent = transactionCounter.getSummaryDataVersion().getSummaryData().getType();
            if (typeOfComponent.equals(SummaryDataType.RULE) && state.equals(TransactionCounterState.RUN)) {
                checkIfAllRelationsEstablished(
                        transactionCounter.getSummaryDataVersion().getSummaryFields(),
                        summaryDataVersionIdModel,
                        transactionCounter.getSummaryDataVersion().getId()
                );
            } else if (typeOfComponent.equals(SummaryDataType.CONNECTOR_INPUT) || typeOfComponent.equals(SummaryDataType.CONNECTOR_OUTPUT)) {
                Long sDVConnectorId = transactionCounter.getSummaryDataVersion().getId();
                if (state.equals(TransactionCounterState.RUN)) {
                    connectorCronService.start(sDVConnectorId, typeOfComponent);
                } else if (state.equals(TransactionCounterState.STOP) || state.equals(TransactionCounterState.PAUSE)) {
                    connectorCronService.stop(sDVConnectorId);
                }
            }

            transactionCounter.setState(state);
        });

        List<TransactionCounterDTO> transactionCounterDTOs = transactionCounters
                .stream()
                .map(transactionCounterMapper)
                .toList();

        putListToRedis(summaryDataVersionIdModel, transactionCounterDTOs);
        transactionCounterRepository.saveAll(transactionCounters);


        return summaryDataIdModel;
    }

    @Override
    @Transactional
    public void incrementAmountErrors(Long sDVModelId, Long sDVComponentId, int delta) {
        TransactionCounter transactionCounter = transactionCounterRepository
                .findByIdWithWriteLock(sDVModelId, sDVComponentId);

        transactionCounter.incrementErrors(delta);
        transactionCounterRepository.save(transactionCounter);
    }

    @Override
    @Transactional
    public void incrementAmountTransactions(Long sDVModelId, Long sDVComponentId) {
        TransactionCounter transactionCounter = transactionCounterRepository
                .findByIdWithWriteLock(sDVModelId, sDVComponentId);

        transactionCounter.incrementTransactionsAmount();
        transactionCounterRepository.save(transactionCounter);
    }

    @Override
    public void updateLastError(Long sDVModelId, Long sDVComponentId, String error) {
        TransactionCounter transactionCounter = transactionCounterRepository
                .findByModelStructSummaryDataVersionIdAndSummaryDataVersionId(sDVModelId, sDVComponentId);

        transactionCounter.setLastError(error);
        transactionCounterRepository.save(transactionCounter);
    }

    public void putListToRedis(Long summaryDataVersionIdModel, List<TransactionCounterDTO> transactionCounterDTOs) {
        String hashKey = transactionCounterRedisPrefix + ":" + summaryDataVersionIdModel;
        transactionCounterDTOs.forEach(transactionCounterDTO -> {
            String key = transactionCounterDTO.getSummaryDataVersionId().toString();
            redisUtil.putValue(hashKey, key, transactionCounterDTO);
        });
    }
}
