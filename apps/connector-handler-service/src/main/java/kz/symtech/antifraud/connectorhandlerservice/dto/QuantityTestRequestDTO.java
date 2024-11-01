package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuantityTestRequestDTO {
    private Map<String, Object> testData;
    private String condition;
    private String resCondition;
}
