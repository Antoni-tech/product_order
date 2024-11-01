package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QualityTestResponseDTO {
    private int number;
    private String condition;
    private Object result;
}
