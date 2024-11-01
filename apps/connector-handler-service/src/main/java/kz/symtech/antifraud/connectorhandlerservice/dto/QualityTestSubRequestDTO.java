package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityTestSubRequestDTO {
    private int number;
    private String condition;
}
