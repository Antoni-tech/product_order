package kz.symtech.antifraud.coreservice.facade;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.services.FieldRelationService;
import kz.symtech.antifraud.coreservice.services.ModelService;
import kz.symtech.antifraud.coreservice.services.SummaryDataVersionService;
import kz.symtech.antifraud.coreservice.services.TransactionCounterService;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.FieldRelationDTO;
import kz.symtech.antifraud.models.dto.model.ModelRuleCacheDTO;
import kz.symtech.antifraud.models.dto.model.TransactionCounterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionFacade {
    private final SummaryDataVersionService summaryDataVersionService;
    private final ModelService modelService;
    private final FieldRelationService fieldRelationService;
    private final TransactionCounterService transactionCounterService;


    public SummaryDataVersion getBySummaryDataIdAndActive(UUID summaryDataId) {
        return summaryDataVersionService.getBySummaryDataIdAndActive(summaryDataId);
    }

    public Map<Long, ModelRuleCacheDTO> getModelRulesCache(Map<Long, ComponentModel> modelRuleComponents) {
        return modelService.getModelRulesCacheDTOByIds(modelRuleComponents);
    }

    public List<FieldRelationDTO> getFieldRelations(Long sDVModelId) {
        return fieldRelationService.getFieldRelations(sDVModelId);
    }

    public Map<Long, TransactionCounterDTO> getTransactionCounters(Long sDVModelId) {
        return transactionCounterService.getAllBySDVModelId(sDVModelId);
    }

    public List<ComponentModel> getModelComponents(Long sDVModelId) {
        return modelService.getModelComponents(sDVModelId);
    }
}
