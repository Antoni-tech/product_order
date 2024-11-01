package kz.symtech.antifraud.connectorhandlerservice.dto;

import kz.symtech.antifraud.connectorhandlerservice.enums.TransactionInputDataType;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInputDataDTO implements Serializable {
    private Long summaryDataVersionModelId;
    private Long summaryDataVersionConnectorId;
    private TransactionInputDataType type;
    private Map<String, Object> data;
}

