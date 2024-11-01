package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QualityTestRequestDTO {
    private Map<String, Object> testData;
    private List<QualityTestSubRequestDTO> conditions;
}

