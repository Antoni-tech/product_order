package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.rules.RuleExpression;
import kz.symtech.antifraud.coreservice.mapper.interfaces.RuleExpressionFunction;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.QualityOperationDTO;
import kz.symtech.antifraud.models.dto.model.RuleExpressionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RuleExpressionCacheMapper implements RuleExpressionFunction {

    private final QualityOperationCacheMapper qualityOperationCacheMapper;

    @Override
    public RuleExpressionDTO apply(RuleExpression ruleExpression, ComponentModel componentModel) {

        QualityOperationDTO qualityOperationDTO = Objects.isNull(ruleExpression.getQualityOperation()) ? null
                : qualityOperationCacheMapper.apply(ruleExpression.getQualityOperation(), componentModel);

        return RuleExpressionDTO
                .builder()
                .expression(ruleExpression.getExpression())
                .type(ruleExpression.getType())
                .qualityOperationDTO(qualityOperationDTO)
                .build();
    }
}
