package kz.symtech.antifraud.connectorhandlerservice.services.impl;

import kz.symtech.antifraud.connectorhandlerservice.dto.*;
import kz.symtech.antifraud.connectorhandlerservice.exceptions.InvalidRuleException;
import kz.symtech.antifraud.connectorhandlerservice.services.RuleEvaluateService;
import kz.symtech.antifraud.models.dto.model.SummaryFieldCacheDTO;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleEvaluateServiceImpl implements RuleEvaluateService {

    private static final String PREVIOUS_PREFIX = "prev";

    @Override
    public Map<String, Object> validateFields(List<SummaryFieldCacheDTO> summaryFields, Map<String, Object> transaction) {
        List<String> errors = new ArrayList<>();

        validate(
                summaryFields,
                summaryFields.stream().filter(x -> x.getParentSummaryField() == null).toList(),
                transaction,
                errors);

        return new HashMap<>() {{
            put(Fields.ERRORS_FIELD, errors);
            put(Fields.ERRORS_COUNT, errors.size());
        }};
    }

    @Override
    public Boolean evaluateRuleMap(ConnectorInputEvaluateData connectorInputEvaluateData, String rule) {
        StandardEvaluationContext context = new StandardEvaluationContext(connectorInputEvaluateData.getMap());

        if (Objects.isNull(connectorInputEvaluateData.getPrevMap())) {
            context.setVariable(PREVIOUS_PREFIX, new HashMap<>());
        } else {
            context.setVariable(PREVIOUS_PREFIX, connectorInputEvaluateData.getPrevMap());
        }
        context.addPropertyAccessor(new MapAccessor());

        ExpressionParser parser = new SpelExpressionParser();
        try {
            return Boolean.TRUE.equals(parser.parseExpression(rule).getValue(context, Boolean.class));
        } catch (SpelEvaluationException e) {
            handleInvalidRuleException(e);
            throw new InvalidRuleException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidRuleException(e.getMessage());
        }
    }

    @Override
    public ConnectorInputEvaluateData evaluateResultMap(ConnectorInputEvaluateData validatedConnectorInputPairDTO, String rule) {
        Map<String, Object> mapForExpression = new HashMap<>(validatedConnectorInputPairDTO.getMap());

        StandardEvaluationContext context = new StandardEvaluationContext(mapForExpression);

        if (Objects.isNull(validatedConnectorInputPairDTO.getPrevMap())) {
            context.setVariable(PREVIOUS_PREFIX, new HashMap<>());
        } else {
            context.setVariable(PREVIOUS_PREFIX, validatedConnectorInputPairDTO.getPrevMap());
        }
        context.addPropertyAccessor(new MapAccessor());

        ExpressionParser parser = new SpelExpressionParser();
        Object result;
        try {
            result = parser.parseExpression(rule).getValue(context, Object.class);
        } catch (SpelEvaluationException e) {
            handleInvalidRuleException(e);
            throw new InvalidRuleException(e.getMessage());
        } catch (Exception e) {
            throw new InvalidRuleException(e.getMessage());
        }

        validatedConnectorInputPairDTO.getMap().put(Fields.RESULT_FIELD, result);
        return validatedConnectorInputPairDTO;
    }

    @Override
    public QuantityTestResponseDTO testQuantityRule(QuantityTestRequestDTO quantityTestRequestDTO) {
        if (quantityTestRequestDTO.getCondition().isEmpty() || quantityTestRequestDTO.getResCondition().isEmpty()) {
            return QuantityTestResponseDTO.builder().conditionSuccess(false).result(Fields.UNDEFINED).build();
        }

        ConnectorInputEvaluateData connectorInputEvaluateData = ConnectorInputEvaluateData
                .builder()
                .map(quantityTestRequestDTO.getTestData())
                .build();

        Boolean isValid = evaluateRuleMap(connectorInputEvaluateData, quantityTestRequestDTO.getCondition());
        Object result = Fields.UNDEFINED;

        if (isValid) {
            ConnectorInputEvaluateData resultData = evaluateResultMap(connectorInputEvaluateData, quantityTestRequestDTO.getResCondition());
            result = resultData.getMap().get(Fields.RESULT_FIELD);
        }

        return QuantityTestResponseDTO.builder().conditionSuccess(isValid).result(result).build();
    }

    @Override
    public List<QualityTestResponseDTO> testQualityRule(QualityTestRequestDTO qualityTestRequestDTO) {
        ConnectorInputEvaluateData connectorInputEvaluateData = ConnectorInputEvaluateData
                .builder()
                .map(qualityTestRequestDTO.getTestData())
                .build();

        List<QualityTestResponseDTO> responseList = new ArrayList<>();

        qualityTestRequestDTO.getConditions().forEach(subData -> {
            int number = subData.getNumber();
            String condition = subData.getCondition();

            String result;
            try {
                Boolean res = evaluateRuleMap(connectorInputEvaluateData, condition);
                result = res.toString();
            } catch (InvalidRuleException invalidRuleException) {
                result = invalidRuleException.getMessage();
            }

            QualityTestResponseDTO qualityTestResponseDTO = QualityTestResponseDTO
                    .builder()
                    .number(number)
                    .condition(condition)
                    .result(result)
                    .build();

            responseList.add(qualityTestResponseDTO);
        });

        return responseList;
    }


    private void handleInvalidRuleException(SpelEvaluationException e) {
        switch (e.getMessageCode()) {
            case PROPERTY_OR_FIELD_NOT_READABLE ->
                    throw new InvalidRuleException(String.format("Property or field '%s' cannot be found", e.getInserts()[0]));
            case OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES -> throw new InvalidRuleException(
                    String.format("The operator '%s' is not supported between objects of type %s and %s",
                            e.getInserts()[0],
                            e.getInserts()[1].getClass().getSimpleName(),
                            e.getInserts()[2].getClass().getSimpleName()));
            case NOT_COMPARABLE ->
                    throw new InvalidRuleException("Cannot compare instances, please check validity of types");
        }
    }

    private void validate(
            List<SummaryFieldCacheDTO> summaryFieldList,
            List<SummaryFieldCacheDTO> summaryFieldChildrenList,
            Map<String, Object> transaction,
            List<String> errors) {

        summaryFieldChildrenList.forEach(summaryField -> {
            if (summaryField.getType().equals(SummaryFieldType.OBJECT)) {
                List<SummaryFieldCacheDTO> children = summaryFieldList
                        .stream()
                        .filter(x -> x.getParentSummaryField() != null)
                        .filter(x -> x.getParentSummaryField().getId().equals(summaryField.getId()))
                        .toList();
                Map<String, Object> childTransaction = ObjectMapperUtils.fromObjectToMap(transaction.get(summaryField.getName()));
                validate(summaryFieldList, children, childTransaction, errors);
            } else {
                if (transaction.containsKey(summaryField.getName())) {
                    Object value = transaction.get(summaryField.getName());
                    if (!checkMaxSize(value, summaryField.getSummaryFieldSubDataDTO().getMaxSize())) {
                        errors.add(String.format("%s - Value %s exceeded maximum size %s",
                                summaryField.getName(), value, summaryField.getSummaryFieldSubDataDTO().getMaxSize()));
                    }

                    if (summaryField.getSummaryFieldSubDataDTO().getAllowEmpty().equals(false)) {
                        if (!checkNotEmpty(value.toString())) {
                            errors.add(String.format("%s - field must not be empty", summaryField.getName()));
                        }
                    }

                    if (summaryField.getSummaryFieldSubDataDTO().getProhibitSpecCharacters().equals(false)) {
                        if (!checkSpecCharacters(value.toString())) {
                            errors.add(String.format("%s - field must not have special characters", summaryField.getName()));
                        }
                    }
                }
            }
        });
    }

    private Boolean checkMaxSize(Object value, int maxSize) {
        if (value == null) {
            return false;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue() <= maxSize;
        } else if (value instanceof String) {
            return ((String) value).length() <= maxSize;
        }
        return false;
    }

    private Boolean checkNotEmpty(String value) {
        return !value.isEmpty();
    }

    private Boolean checkSpecCharacters(String value) {
        String patternSpecCharacters = "[^a-zA-Z0-9]";

        Pattern pattern = Pattern.compile(patternSpecCharacters);
        Matcher matcher = pattern.matcher(value);

        return !matcher.find();
    }
}
