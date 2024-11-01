package kz.symtech.antifraud.connectorhandlerservice.services.impl;

import kz.symtech.antifraud.connectorhandlerservice.dto.TransactionInputDataDTO;
import kz.symtech.antifraud.connectorhandlerservice.dto.TransactionInputDataDTORule;
import kz.symtech.antifraud.connectorhandlerservice.entities.TransactionInputData;
import kz.symtech.antifraud.connectorhandlerservice.repositories.TransactionInputDataRepository;
import kz.symtech.antifraud.connectorhandlerservice.services.TransactionInputDataService;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionInputDataServiceImpl implements TransactionInputDataService {

    private final TransactionInputDataRepository transactionInputDataRepository;

    @Override
    public void save(TransactionInputDataDTO transactionInputDataDTO) {
        saveTransactionInputData(transactionInputDataDTO, null);
    }

    @Override
    public void save(TransactionInputDataDTORule transactionInputDataDTORule) {
        saveTransactionInputData(transactionInputDataDTORule, transactionInputDataDTORule.getSummaryDataVersionRuleId());
    }

    private void saveTransactionInputData(TransactionInputDataDTO dto, Long summaryDataVersionRuleId) {
        TransactionInputData transactionInputData = transactionInputDataRepository
                .findBySummaryDataVersionModelIdAndSummaryDataVersionConnectorIdAndSummaryDataVersionRuleId(
                        dto.getSummaryDataVersionModelId(), dto.getSummaryDataVersionConnectorId(), summaryDataVersionRuleId);

        if (Objects.isNull(transactionInputData)) {
            transactionInputData = new TransactionInputData();
        }

        transactionInputData.setType(dto.getType());
        transactionInputData.setJsonData(ObjectMapperUtils.getJson(dto.getData()));
        transactionInputData.setSummaryDataVersionModelId(dto.getSummaryDataVersionModelId());
        transactionInputData.setSummaryDataVersionConnectorId(dto.getSummaryDataVersionConnectorId());
        transactionInputData.setSummaryDataVersionRuleId(summaryDataVersionRuleId);

        transactionInputDataRepository.save(transactionInputData);
    }

    @Override
    public TransactionInputDataDTORule get(Long sDVModelId, Long sDVConnectorId, Long sDVRuleId) {
        TransactionInputData transactionInputData = transactionInputDataRepository
                .findBySummaryDataVersionModelIdAndSummaryDataVersionConnectorIdAndSummaryDataVersionRuleId(
                        sDVModelId, sDVConnectorId, sDVRuleId);

        if (Objects.isNull(transactionInputData)) {
//            throw new NotFoundException(String.format("There is no data of transaction data with model id '%s', connector '%s', rule '%s'",
//                    sDVModelId, sDVConnectorId, sDVRuleId));
            return null;
        }

        return buildDataRule(transactionInputData);
    }

    private TransactionInputDataDTO buildDataConnector(TransactionInputData transactionInputData) {
        return TransactionInputDataDTO.builder()
                .summaryDataVersionModelId(transactionInputData.getSummaryDataVersionModelId())
                .summaryDataVersionConnectorId(transactionInputData.getSummaryDataVersionConnectorId())
                .data(ObjectMapperUtils.fromJsonStringToMap(transactionInputData.getJsonData()))
                .type(transactionInputData.getType())
                .build();
    }

    private TransactionInputDataDTORule buildDataRule(TransactionInputData transactionInputData) {
        TransactionInputDataDTORule transactionInputDataDTORule = new TransactionInputDataDTORule();

        transactionInputDataDTORule.setSummaryDataVersionModelId(transactionInputData.getSummaryDataVersionModelId());
        transactionInputDataDTORule.setSummaryDataVersionConnectorId(transactionInputData.getSummaryDataVersionConnectorId());
        transactionInputDataDTORule.setSummaryDataVersionRuleId(transactionInputData.getSummaryDataVersionRuleId());
        transactionInputDataDTORule.setData(ObjectMapperUtils.fromJsonStringToMap(transactionInputData.getJsonData()));
        transactionInputDataDTORule.setType(transactionInputData.getType());

        return transactionInputDataDTORule;
    }
}
