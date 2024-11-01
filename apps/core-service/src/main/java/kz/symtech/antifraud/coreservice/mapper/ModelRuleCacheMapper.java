package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.models.ModelRule;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.mapper.interfaces.ModelRuleFunction;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.ModelRuleCacheDTO;
import kz.symtech.antifraud.models.dto.model.RuleExpressionDTO;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ModelRuleCacheMapper implements ModelRuleFunction {

    private final RuleExpressionCacheMapper ruleExpressionCacheMapper;

    @Override
    public ModelRuleCacheDTO apply(ModelRule rule, ComponentModel componentModel) {

        List<RuleExpressionDTO> ruleExpressionDTOList = rule.getSummaryDataVersion().getRuleExpressions()
                .stream().map(expression -> ruleExpressionCacheMapper.apply(expression, componentModel)).toList();

        return ModelRuleCacheDTO
                .builder()
                .id(rule.getId())
                .summaryDataVersionId(rule.getSummaryDataVersion().getId())
                .type(rule.getSummarySubType())
                .summaryFieldRuleIds(
                        rule.getSummaryDataVersion().getSummaryFields()
                                .stream()
                                .filter(sf -> sf.getSummaryDataVersion().getSummaryData().getType().equals(SummaryDataType.RULE))
                                .filter(sf -> sf.getDefaultField().equals(false))
                                .map(BaseEntity::getId)
                                .toList()
                )
                .ruleExpressionDTOS(ruleExpressionDTOList)
                .build();
    }

}
