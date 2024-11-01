package kz.symtech.antifraud.connectorhandlerservice.services;

import kz.symtech.antifraud.connectorhandlerservice.dto.ConnectorInputEvaluateData;
import kz.symtech.antifraud.connectorhandlerservice.exceptions.InvalidRuleException;
import kz.symtech.antifraud.models.enums.Fields;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Slf4j
public class RuleEvaluateServiceTest {

    @Autowired
    private RuleEvaluateService ruleEvaluateService;
    private static ConnectorInputEvaluateData connectorInputEvaluateData;

    @BeforeAll
    static void init() {
        connectorInputEvaluateData = ConnectorInputEvaluateData
                .builder()
                .map(new HashMap<>(){{
                    put("a", 100);
                    put("b", "name");
                    put("c", 300);
                    put("d", "password");
                    put("e", 0);
                }})
                .build();
    }

    @ParameterizedTest
    @CsvSource({
            "((a > 100) OR (d == 'password')) AND (b == 'name') AND (c >= 300), true",
            "((a >= 100) OR (d == 'apple')) AND (b == 'name') AND (c > 200), true",
            "((a >= 100) OR (d == 'password')) AND (b != 'name') AND (c > 300), false",
            "((a >= 100) OR (d == 'password')) AND (b == 'name') AND (c < 300), false"
    })
    public void evaluateRuleSuccess(String rule, Boolean expected) {
        Boolean result = ruleEvaluateService.evaluateRuleMap(connectorInputEvaluateData, rule);
        assertEquals(result, expected);
    }

    @ParameterizedTest
    @CsvSource({
            "(b > 100), 'Cannot compare instances, please check validity of types'",
            "(z == 'text'), Property or field 'z' cannot be found",
            "(a / e == 10), / by zero"
    })
    public void evaluateRuleThrowsInvalidRuleException(String rule, String message) {
        Exception exception = assertThrows(InvalidRuleException.class,
                () -> ruleEvaluateService.evaluateRuleMap(connectorInputEvaluateData, rule));
        assertEquals(message, exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "c / a, 3",
            "c + a, 400",
            "c * a, 30000",
            "c - a, 200",
            "c = 20, 20"
    })
    public void evaluateResult(String resCondition, Object resultValue) {
        ConnectorInputEvaluateData connectorInputEvaluateDataRes =
                ruleEvaluateService.evaluateResultMap(connectorInputEvaluateData, resCondition);

        assertEquals(resultValue.toString(), connectorInputEvaluateDataRes.getMap().get(Fields.RESULT_FIELD).toString());
    }

    @ParameterizedTest
    @CsvSource({
            "d * b",
            "d - b",
            "d / b"
    })
    public void test(String redCondition) {
        assertThrows(InvalidRuleException.class, () -> ruleEvaluateService.evaluateResultMap(connectorInputEvaluateData, redCondition));
    }
}
