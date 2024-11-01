package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.ModelRuleDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelRuleDTOMapper {
    public ModelRuleDTO buildModelRuleDTO(ModelRule modelRule) {
        return ModelRuleDTO.builder()
                .id(modelRule.getSummaryDataVersion().getId())
                .type(modelRule.getSummaryDataVersion().getSummaryData().getType())
                .name(modelRule.getSummaryDataVersion().getName())
                .createUserId(modelRule.getSummaryDataVersion().getUserUpdateId())
                .ruleType(modelRule.getSummarySubType())
                .build();
    }
}
