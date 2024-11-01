package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.facade.TransactionFacade;
import kz.symtech.antifraud.coreservice.services.ModelResponseBuildService;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.ModelStructResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelResponseBuildServiceImpl implements ModelResponseBuildService {

    private final TransactionFacade transactionFacade;

    @Override
    public ModelStructResponseDTO getModelResponseDTO(UUID summaryDataId) {
        ModelStructResponseDTO modelStructResponseDTO = new ModelStructResponseDTO();

        SummaryDataVersion summaryDataVersion = transactionFacade.getBySummaryDataIdAndActive(summaryDataId);
        Long sDVModelId = summaryDataVersion.getId();

        modelStructResponseDTO.setSummaryDataVersionId(sDVModelId);
        modelStructResponseDTO.setComponents(transactionFacade.getModelComponents(sDVModelId));
        modelStructResponseDTO.setFieldRelations(transactionFacade.getFieldRelations(sDVModelId));
        modelStructResponseDTO.setTransactionCounterDTOMap(transactionFacade.getTransactionCounters(sDVModelId));

        Map<Long, ComponentModel> modelRuleComponents = getModelRuleComponents(modelStructResponseDTO.getComponents());
        modelStructResponseDTO.setRuleMap(transactionFacade.getModelRulesCache(modelRuleComponents));

        return modelStructResponseDTO;
    }

    private Map<Long, ComponentModel> getModelRuleComponents(List<ComponentModel> componentModels) {
        return componentModels
                .stream()
                .filter(component -> component.getQueueNumber() != -1) // components that are not a rule have queueNumber = -1
                .collect(Collectors.toMap(ComponentModel::getSummaryDataVersionId, Function.identity()));
    }


}
