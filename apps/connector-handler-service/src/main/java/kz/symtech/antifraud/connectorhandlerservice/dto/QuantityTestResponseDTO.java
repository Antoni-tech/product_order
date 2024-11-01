package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuantityTestResponseDTO {
    private Boolean conditionSuccess;
    private Object result;
}
