package kz.symtech.antifraud.testservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import kz.symtech.antifraud.testservice.dto.TransactionDataRequestDto;
import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import kz.symtech.antifraud.testservice.repositories.TransactionDataRepository;
import kz.symtech.antifraud.testservice.services.CommonService;
import kz.symtech.antifraud.testservice.services.TransactionDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service("transactionDataService")
@RequiredArgsConstructor
public class TransactionDataServiceImpl implements TransactionDataService, CommonService {

    private static final int BATCH_SIZE = 1000;

    private final TransactionDataRepository transactionDataRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Long saveTransactionData(TransactionDataRequestDto transactionDataRequestDto) {
        TransactionData transactionData = null;
        if (Objects.nonNull(transactionDataRequestDto.getId())) {
            transactionData = transactionDataRepository.findById(transactionDataRequestDto.getId()).orElse(null);
        }

        TransactionData updateTransactionData = createOrUpdateTransactionData(transactionData, transactionDataRequestDto);
        return transactionDataRepository.save(updateTransactionData).getId();
    }

    @Override
    public void updateState(Long transactionDataId, State state) {
        TransactionData transactionData = transactionDataRepository.findById(transactionDataId)
                .orElseThrow(() -> new NotFoundException("Transaction data not found"));

        transactionData.setState(state);
        transactionDataRepository.save(transactionData);
    }

    @Override
    public TransactionData get(Long id) {
        return transactionDataRepository.findById(id).orElseThrow(() -> new NotFoundException("transaction data not found"));
    }

    @Override
    public List<TransactionData> getAll() {
        List<TransactionData> transactionDataList = new ArrayList<>();
        int pageNumber = 0;

        while(true) {
            Pageable pageable = PageRequest.of(pageNumber, BATCH_SIZE);
            Page<TransactionData> transactionDataPage = transactionDataRepository.findAll(pageable);

            List<TransactionData> transactionDataListPart = transactionDataPage.getContent();
            if (transactionDataListPart.isEmpty()) {
                break;
            }

            transactionDataList.addAll(transactionDataListPart);
            pageNumber++;
        }

        return transactionDataList;
    }

    @Override
    public List<TransactionData> getAllByState(State state) {
        return transactionDataRepository.findAllByState(state);
    }

    @Override
    public void delete(Long id) {
        transactionDataRepository.deleteById(id);
    }

    @Override
    public TransactionData getBySummaryDataModelIdAndSummaryDataConnectorId(UUID summaryDataModelId, UUID summaryDataConnectorId) {
        return transactionDataRepository.findBySummaryDataModelIdAndSummaryDataConnectorId(summaryDataModelId, summaryDataConnectorId);
    }

    private TransactionData createOrUpdateTransactionData(TransactionData existingData, TransactionDataRequestDto requestDto) {
        TransactionData transactionData = existingData != null ? existingData : new TransactionData();

        transactionData.setSummaryDataModelId(requestDto.getSummaryDataModelId());
        transactionData.setSummaryDataConnectorId(requestDto.getSummaryDataConnectorId());
        transactionData.setUserId(requestDto.getUserId());
        transactionData.setNumberOfRepetitions(requestDto.getNumberOfRepetitions());
        transactionData.setIsCyclical(requestDto.getIsCyclical());
        transactionData.setDelay(requestDto.getDelay());
        transactionData.setChronoUnit(requestDto.getChronoUnit());
        transactionData.setExpiresAt(requestDto.getExpiresAt().toLocalDateTime());
        transactionData.setState(State.COMPLETED);
        transactionData.setDelayToReplace(requestDto.getDelayToReplace());
        transactionData.setChronoUnitToReplace(requestDto.getChronoUnitToReplace());

        try {
            transactionData.setData(objectMapper.writeValueAsString(requestDto.getData()));
            transactionData.setDataToReplace(objectMapper.writeValueAsString(requestDto.getDataToReplace()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return transactionData;
    }
}
